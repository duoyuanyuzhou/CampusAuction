package org.multiverse.campusauction.notification.consumer;

import lombok.extern.slf4j.Slf4j;
import org.multiverse.campusauction.notification.event.NotificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.multiverse.campusauction.notification.handler.NotificationHandler;
import java.util.Map;

@Component
@Slf4j
public class NotificationConsumer {

    @Autowired
    private Map<String, NotificationHandler> handlerMap;

    public void consume(NotificationEvent event) {

        // 1️⃣ 幂等校验
        if (alreadyNotified(event)) {
            return;
        }

        // 2️⃣ 找对应处理器
        NotificationHandler handler =
                handlerMap.get(event.getType());

        if (handler == null) {
            log.warn("无通知处理器 type={}", event.getType());
            return;
        }

        // 3️⃣ 处理通知
        handler.handle(event);
    }

    private boolean alreadyNotified(NotificationEvent event) {
        // userId + type + bizId 唯一
        return false;
    }
}
