package org.multiverse.campusauction.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.multiverse.campusauction.entity.domain.UserMessage;
import org.multiverse.campusauction.notification.event.NotificationEvent;

/**
* @author jiaxi
* @description 针对表【user_message(用户消息表)】的数据库操作Service
* @createDate 2025-11-29 17:28:16
*/
public interface UserMessageService extends IService<UserMessage> {

    UserMessage createUserMessage(NotificationEvent event);

    Page<UserMessage> getUserMessagePage(Page<UserMessage> page, UserMessage userMessage);

}
