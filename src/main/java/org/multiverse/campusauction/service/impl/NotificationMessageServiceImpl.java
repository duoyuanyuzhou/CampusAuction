package org.multiverse.campusauction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.multiverse.campusauction.entity.domain.NotificationMessage;
import org.multiverse.campusauction.service.NotificationMessageService;
import org.multiverse.campusauction.mapper.NotificationMessageMapper;
import org.springframework.stereotype.Service;

/**
* @author jiaxi
* @description 针对表【notification_message(站内通知消息表)】的数据库操作Service实现
* @createDate 2025-12-16 14:09:02
*/
@Service
public class NotificationMessageServiceImpl extends ServiceImpl<NotificationMessageMapper, NotificationMessage>
    implements NotificationMessageService{

    /**
     * 创建通知消息
     */
    public NotificationMessage createMessage(
            Long userId,
            String title,
            String type,
            String content,
            String bizType,
            Long bizId
    ) {
        NotificationMessage message = new NotificationMessage();
        message.setUserId(userId);
        message.setTitle(title);
        message.setType(type);
        message.setContent(content);
        message.setBizType(bizType);
        message.setBizId(bizId);
        message.setIsRead(0);

        this.save(message);

        // 可以在这里更新 Redis 未读数或推送
        return message;
    }


}




