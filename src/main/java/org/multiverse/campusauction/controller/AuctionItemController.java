package org.multiverse.campusauction.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.multiverse.campusauction.annotation.CheckLogin;
import org.multiverse.campusauction.annotation.CheckPermission;
import org.multiverse.campusauction.constant.RedisKeyConstants;
import org.multiverse.campusauction.entity.domain.AuctionItem;
import org.multiverse.campusauction.entity.vo.ApiResponse;
import org.multiverse.campusauction.exception.ApiException;
import org.multiverse.campusauction.service.AuctionItemService;
import org.multiverse.campusauction.service.UserService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


@RestController("/auctionItem")
public class AuctionItemController {

    @Autowired
    private AuctionItemService auctionItemService;

    @Autowired
    private  RedisTemplate<String, Object> redisTemplate;


    @Autowired
    private UserService userService;

    @GetMapping("/getPage")
    public ApiResponse getPage(@RequestParam("page")Integer page, @RequestParam("size") Integer size, @ParameterObject AuctionItem auctionItem){
        Page<AuctionItem> auctionItemsPage = auctionItemService.getAuctionItemsPage(page, size, auctionItem, null);
        return ApiResponse.ok(auctionItemsPage);
    }

    @CheckPermission
    @GetMapping("/getPageByAdmin")
    public ApiResponse getPageByAdmin(@RequestParam("page")Integer page, @RequestParam("size") Integer size, @ParameterObject AuctionItem auctionItem){
        Page<AuctionItem> auctionItemsPage = auctionItemService.getAuctionItemsPageByAdmin(page, size, auctionItem);
        return ApiResponse.ok(auctionItemsPage);
    }

    @CheckLogin
    @GetMapping("/getPageByUser")
    public ApiResponse getPageByUser(int page, int size, @ParameterObject AuctionItem auctionItem){
        Long userId = StpUtil.getLoginIdAsLong();
        Page<AuctionItem> auctionItemsPage = auctionItemService.getPageByUser(page, size, auctionItem, userId);
        return ApiResponse.ok(auctionItemsPage);
    }

    @CheckLogin
    @PostMapping("/addAuctionItem")
    public ApiResponse addAuctionItem(@RequestBody AuctionItem auctionItem){
        Long userId = StpUtil.getLoginIdAsLong();
        auctionItem.setUserId(userId);
        boolean save = auctionItemService.save(auctionItem);
        if(save) {
            return ApiResponse.ok("提交成功");
        }else {
            return ApiResponse.fail("提交失败");
        }
    }
    @GetMapping("/auctionItem/{id}")
    public ApiResponse getAuctionItem(@PathVariable("id") Integer id) {
        AuctionItem auctionItem = auctionItemService.getById(id);
        return ApiResponse.ok(auctionItem);
    }

    @GetMapping("/inProgressAuctionItem/{id}")
    public ApiResponse<AuctionItem> getInProgressAuctionItem(
            @PathVariable("id") Long id) {

        String redisKey = RedisKeyConstants.AUCTION_AUDIT_DELAY + id;

        // 1️⃣ 先查 Redis
        AuctionItem auctionItem =
                (AuctionItem) redisTemplate.opsForValue().get(redisKey);

        if (auctionItem != null) {
            return ApiResponse.ok(auctionItem);
        }

        // 2️⃣ Redis 没有，再查数据库
        auctionItem = auctionItemService.getOne(
                new LambdaQueryWrapper<AuctionItem>()
                        .eq(AuctionItem::getId, id)
                        .in(AuctionItem::getStatus, 2, 3)
        );

        if (auctionItem == null) {
            return ApiResponse.fail("拍卖不存在或未进行中");
        }

        // 3️⃣ 写入 Redis（设置过期时间）
        long ttlSeconds = Duration
                .between(LocalDateTime.now(), auctionItem.getEndTime())
                .getSeconds();

        if (ttlSeconds > 0) {
            redisTemplate.opsForValue().set(
                    redisKey,
                    auctionItem,
                    ttlSeconds,
                    TimeUnit.SECONDS
            );
        }


        return ApiResponse.ok(auctionItem);
    }


    @DeleteMapping("/delete/:id")
    public ApiResponse deleteAuctionItem(@Param("id") Integer id){
        boolean b = auctionItemService.removeById(id);
        if(b) {
            return ApiResponse.ok("删除成功");
        }else {
            return ApiResponse.fail("删除失败");
        }
    }
    @CheckPermission
    @PostMapping("/reviewAuctionItem")
    public ApiResponse reviewAuctionItem(@RequestBody AuctionItem auctionItem){
        LambdaUpdateWrapper<AuctionItem> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AuctionItem::getId,auctionItem.getId())
                        .set(AuctionItem::getAuditComment,auctionItem.getAuditComment())
                .set(AuctionItem::getStatus,auctionItem.getStatus());
        boolean update = auctionItemService.update(updateWrapper);
        if(update) {
            return ApiResponse.ok("修改成功");
        }else {
            return ApiResponse.fail("修改失败");
        }
    }

    @PutMapping("/update/{id}")
    public ApiResponse updateAuctionItem(@PathVariable("id") Integer id, @RequestBody AuctionItem auctionItem){
        Object loginIdObj = null;
        try {
            loginIdObj = StpUtil.getLoginId();
        }catch (Exception e){
            throw new ApiException(555,"token过期");
        }
        if (loginIdObj == null) {
            return ApiResponse.fail("没有登陆，请登录");
        }

        Long loginId = Long.valueOf(loginIdObj.toString());
        boolean b = auctionItemService.updateById(auctionItem);
        if(b) {
            return ApiResponse.ok("更新成功");
        }else {
            return ApiResponse.fail("更新失败");
        }
    }


}
