package org.multiverse.campusauction.notification.handler;

import org.multiverse.campusauction.notification.event.NotificationEvent;

public interface NotificationHandler {

    /**
     * 通知类型（用于路由）
     */
    String getType();

    /**
     * 处理通知
     */
    void handle(NotificationEvent event);
}
