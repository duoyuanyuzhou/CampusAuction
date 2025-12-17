package org.multiverse.campusauction.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.multiverse.campusauction.annotation.CheckLogin;
import org.multiverse.campusauction.entity.domain.OrderInfo;
import org.multiverse.campusauction.entity.domain.UserMessage;
import org.multiverse.campusauction.entity.vo.ApiResponse;
import org.multiverse.campusauction.entity.vo.OrderInfoVo;
import org.multiverse.campusauction.service.UserMessageService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userMessage")
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;

    @CheckLogin
    @GetMapping("/getPage")
    public ApiResponse<Page<UserMessage>> getUserMessagePage(@ParameterObject Page<UserMessage> page, @ParameterObject UserMessage userMessage) {
        Long userId = StpUtil.getLoginIdAsLong();
        userMessage.setUserId(userId);
        Page<UserMessage> userMessagePage = userMessageService.getUserMessagePage(page, userMessage);

        return ApiResponse.ok(userMessagePage);
    }
}
