package org.multiverse.campusauction.service;

import org.multiverse.campusauction.entity.domain.NotificationMessage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author jiaxi
* @description 针对表【notification_message(站内通知消息表)】的数据库操作Service
* @createDate 2025-12-16 14:09:02
*/
public interface NotificationMessageService extends IService<NotificationMessage> {
    public NotificationMessage createMessage(
            Long userId,
            String title,
            String type,
            String content,
            String bizType,
            Long bizId
    );
}
