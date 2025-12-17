package org.multiverse.campusauction.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.multiverse.campusauction.annotation.CheckLogin;
import org.multiverse.campusauction.constant.RedisKeyConstants;
import org.multiverse.campusauction.entity.domain.AuctionItem;
import org.multiverse.campusauction.entity.domain.BidRecord;
import org.multiverse.campusauction.entity.vo.ApiResponse;
import org.multiverse.campusauction.exception.ApiException;
import org.multiverse.campusauction.service.AuctionItemService;
import org.multiverse.campusauction.service.BidRecordService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.multiverse.campusauction.constant.AuctionConstants.*;
import static org.multiverse.campusauction.constant.RedisKeyConstants.AUCTION_END_DELAY;

@RestController
@RequestMapping("/bidRecord")
class BidRecordController {
    @Autowired
    BidRecordService bidRecordService;

    @Autowired
    private AuctionItemService  auctionItemService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private final Lock rlock = rwlock.readLock();
    private final Lock wlock = rwlock.writeLock();

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);
        try {
            emitter.send(SseEmitter.event().name("init").data("connected"));
        } catch (IOException e) {
            emitters.remove(emitter);
        }
        // 客户端断开时移除
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    @CheckLogin
    @GetMapping("/getPage")
    public ApiResponse<Page<BidRecord>> getPage(@ParameterObject Page<BidRecord> page, @ParameterObject BidRecord bidRecord) {
        rlock.lock();
        Page<BidRecord> bidRecordPage;
        try {
            bidRecordPage = bidRecordService.getBidRecordPage(page, bidRecord);
        }finally {
            rlock.unlock();
        }
        return ApiResponse.ok(bidRecordPage);
    }

    @GetMapping("/getBidRecords")
    public ApiResponse<List<BidRecord>> getBidRecords(@ParameterObject BidRecord bidRecord) {
        rlock.lock();
        List<BidRecord> bidRecords;
        try {
            LambdaQueryWrapper<BidRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BidRecord::getItemId,bidRecord.getItemId());
            bidRecords = bidRecordService.list(queryWrapper);
        }finally {
            rlock.unlock();
        }
        return ApiResponse.ok(bidRecords);
    }

    @PostMapping("/addBidRecord")
    public ApiResponse<BidRecord> addBidRecord(@RequestBody BidRecord bidRecord) {

        Long itemId = bidRecord.getItemId();
        if (itemId == null) {
            throw new ApiException(400, "拍卖品ID不能为空");
        }

        // 1️⃣ 查询拍卖品
        AuctionItem auctionItem = auctionItemService.getById(itemId);
        if (auctionItem == null) {
            throw new ApiException(404, "拍卖品不存在");
        }

        LocalDateTime now = LocalDateTime.now();

        // 2️⃣ 基础时间校验（锁外）
        if (now.isBefore(auctionItem.getStartTime())) {
            throw new ApiException(445, "拍卖会未开始");
        }
        if (now.isAfter(auctionItem.getEndTime())) {
            throw new ApiException(446, "拍卖会已结束");
        }

        Long userId = StpUtil.getLoginIdAsLong();

        wlock.lock();
        try {
            // 🔒 3️⃣ 锁内再次校验结束时间（防并发）
            AuctionItem lockedItem = auctionItemService.getById(itemId);
            if (LocalDateTime.now().isAfter(lockedItem.getEndTime())) {
                throw new ApiException(446, "拍卖会已结束");
            }

            // 4️⃣ 查询当前最高出价
            BidRecord highestBid = bidRecordService.getOne(
                    new LambdaQueryWrapper<BidRecord>()
                            .eq(BidRecord::getItemId, itemId)
                            .orderByDesc(BidRecord::getBidPrice)
                            .orderByAsc(BidRecord::getCreateTime)
                            .last("LIMIT 1")
            );

            // 5️⃣ 出价合法性校验
            if (highestBid != null) {
                if (bidRecord.getBidPrice()
                        .compareTo(highestBid.getBidPrice()) <= 0) {
                    throw new ApiException(444, "出价必须高于当前最高价");
                }
            } else {
                if (bidRecord.getBidPrice()
                        .compareTo(lockedItem.getStartPrice()) < 0) {
                    throw new ApiException(444, "出价不能低于起拍价");
                }
            }

            bidRecord.setUserId(userId);
            bidRecord.setCreateTime(LocalDateTime.now());
            bidRecordService.save(bidRecord);

            long remainSeconds = Duration
                    .between(LocalDateTime.now(), lockedItem.getEndTime())
                    .getSeconds();

            if (remainSeconds <= AUTO_EXTEND_THRESHOLD_SECONDS) {

                String extendKey =
                        RedisKeyConstants.AUCTION_EXTEND_COUNT + itemId;

                Long extendCount =
                        redisTemplate.opsForValue().increment(extendKey);

                if (extendCount != null && extendCount <= MAX_EXTEND_TIMES) {

                    LocalDateTime newEndTime =
                            lockedItem.getEndTime()
                                    .plusSeconds(AUTO_EXTEND_SECONDS);

                    lockedItem.setEndTime(newEndTime);
                    auctionItemService.updateById(lockedItem);

                    String redisKey =
                            RedisKeyConstants.AUCTION_AUDIT_DELAY + itemId;

                    long ttlSeconds = Duration
                            .between(LocalDateTime.now(), newEndTime)
                            .getSeconds();

                    String cacheKey = RedisKeyConstants.AUCTION_ITEM_CACHE + itemId;

                    redisTemplate.opsForValue().set(
                            cacheKey,
                            lockedItem,
                            ttlSeconds,
                            TimeUnit.SECONDS
                    );

                    // 3. 更新 ZSet 的 score（最关键）
                    redisTemplate.opsForZSet().add(
                            AUCTION_END_DELAY,
                            itemId.toString(),
                            newEndTime.toEpochSecond(ZoneOffset.UTC)
                    );
                    // 如果你有延时队列，这里同步更新
                    // delayQueueService.update(itemId, newEndTime);

                    // 通知前端：拍卖时间被延长
//                    notifyExtendTime(itemId, newEndTime);
                }
            }

        } finally {
            wlock.unlock();
        }

        // 8️⃣ 推送最新出价给前端
        notifyClients(bidRecord);

        return ApiResponse.ok(bidRecord);
    }


    private void notifyClients(BidRecord bidRecord) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("bid")
                        .data(bidRecord));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }
}
