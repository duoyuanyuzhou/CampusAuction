package org.multiverse.campusauction.job;

import jakarta.annotation.Resource;
import org.multiverse.campusauction.entity.domain.OrderInfo;
import org.multiverse.campusauction.notification.consumer.NotificationConsumer;
import org.multiverse.campusauction.notification.event.NotificationEvent;
import org.multiverse.campusauction.service.OrderInfoService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
public class AuctionEndDelayJob {

    private static final String AUCTION_END_DELAY_KEY = "auction:end:delay";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private OrderInfoService  orderInfoService;



    @Scheduled(fixedDelay = 1000)
    public void consumeAuctionEnd() {

        long now = Instant.now().getEpochSecond();

        Set<Object> items = redisTemplate.opsForZSet()
                .rangeByScore(AUCTION_END_DELAY_KEY, 0, now);

        if (items == null || items.isEmpty()) {
            return;
        }

        for (Object itemIdObj : items) {

            String itemIdStr = itemIdObj.toString();

            Long removed = redisTemplate.opsForZSet()
                    .remove(AUCTION_END_DELAY_KEY, itemIdStr);

            if (removed != null && removed > 0) {
                OrderInfo orderInfo = orderInfoService.creaateOrderInfo(Long.valueOf(itemIdStr));
            }
        }
    }
}
