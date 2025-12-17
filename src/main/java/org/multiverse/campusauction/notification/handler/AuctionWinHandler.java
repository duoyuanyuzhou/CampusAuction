package org.multiverse.campusauction.notification.handler;

import org.multiverse.campusauction.notification.event.NotificationEvent;
import org.multiverse.campusauction.service.NotificationMessageService;
import org.multiverse.campusauction.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuctionWinHandler implements NotificationHandler {

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private NotificationMessageService notificationMessageService;

    @Override
    public String getType() {
        return "AUCTION_WIN";
    }

    @Override
    public void handle(NotificationEvent event) {

        userMessageService.createUserMessage(event);
    }
}
