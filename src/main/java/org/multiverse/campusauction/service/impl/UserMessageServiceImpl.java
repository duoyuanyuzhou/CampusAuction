package org.multiverse.campusauction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.multiverse.campusauction.config.websocket.handler.NotificationWebSocketHandler;
import org.multiverse.campusauction.entity.domain.UserMessage;
import org.multiverse.campusauction.notification.event.NotificationEvent;
import org.multiverse.campusauction.service.UserMessageService;
import org.multiverse.campusauction.mapper.UserMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author jiaxi
* @description 针对表【user_message(用户消息表)】的数据库操作Service实现
* @createDate 2025-11-29 17:28:16
*/
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage>
    implements UserMessageService{

    @Autowired
    private NotificationWebSocketHandler notificationWebSocketHandler;


    @Override
    public UserMessage createUserMessage(NotificationEvent event) {

        UserMessage userMessage = new UserMessage();
        userMessage.setUserId(event.getUserId());
        userMessage.setContent(event.getContent());
        userMessage.setTitle(event.getTitle());
        userMessage.setType(event.getBizId());
        userMessage.setType(0);

        this.save(userMessage);
        notificationWebSocketHandler.sendMessageToUser(event.getUserId(), userMessage);
        return userMessage;
    }

    @Override
    public Page<UserMessage> getUserMessagePage(Page<UserMessage> page, UserMessage userMessage) {
        LambdaQueryWrapper<UserMessage> queryWrapper = new LambdaQueryWrapper<>();
        Page<UserMessage> result = this.page(page, queryWrapper);
        return result;
    }
}




