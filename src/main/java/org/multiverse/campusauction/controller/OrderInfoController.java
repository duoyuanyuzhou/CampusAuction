package org.multiverse.campusauction.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.multiverse.campusauction.annotation.CheckLogin;
import org.multiverse.campusauction.entity.domain.BidRecord;
import org.multiverse.campusauction.entity.domain.OrderInfo;
import org.multiverse.campusauction.entity.vo.ApiResponse;
import org.multiverse.campusauction.entity.vo.OrderInfoVo;
import org.multiverse.campusauction.service.OrderInfoService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/orderInfo")
class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    @CheckLogin
    @GetMapping("/getPage")
    public ApiResponse<Page<OrderInfoVo>> getOrderInfoVoPage(@ParameterObject Page<OrderInfo> page, @ParameterObject OrderInfo orderInfo) {
        Long userId = StpUtil.getLoginIdAsLong();
        orderInfo.setBuyerId(userId);
        Page<OrderInfoVo> orderInfoVoPage = orderInfoService.getOrderInfoPage(page, orderInfo);

        return ApiResponse.ok(orderInfoVoPage);
    }

    @PostMapping("/payOrder/{orderId}")
    public ApiResponse<OrderInfoVo> payOrder(@PathVariable("orderId") Long orderId,@RequestBody  OrderInfoVo orderInfoVo) {
        LambdaUpdateWrapper<OrderInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(OrderInfo::getId, orderId)
                .set(OrderInfo::getPayTime, LocalDateTime.now())
                .set(OrderInfo::getStatus, 1);
        boolean update = orderInfoService.update(updateWrapper);
        return ApiResponse.ok(orderInfoVo);
    }
}
