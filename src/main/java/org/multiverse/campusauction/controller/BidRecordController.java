package org.multiverse.campusauction.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.multiverse.campusauction.annotation.CheckLogin;
import org.multiverse.campusauction.entity.domain.BidRecord;
import org.multiverse.campusauction.entity.vo.ApiResponse;
import org.multiverse.campusauction.service.BidRecordService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RestController
@RequestMapping("/bidRecord")
class BidRecordController {
    @Autowired
    BidRecordService bidRecordService;

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
        wlock.lock();
        Long userId = StpUtil.getLoginIdAsLong();
        bidRecord.setUserId(userId);
        Boolean save;
        try {
            save = bidRecordService.save(bidRecord);
        }finally {
            wlock.unlock();
        }
        if (save) {
            notifyClients(bidRecord);
            return ApiResponse.ok(bidRecord);
        }else {
            return ApiResponse.fail("添加失败，请重试");
        }
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
