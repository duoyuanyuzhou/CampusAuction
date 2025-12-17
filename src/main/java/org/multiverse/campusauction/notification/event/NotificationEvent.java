package org.multiverse.campusauction.notification.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {

    private Long userId;        // 接收人
    private String type;        // AUCTION_WIN / ORDER_PAID
    private Map<String, Object> params; // 模板参数
    private String bizType;     // auction / order
    private Integer bizId;         // 业务ID
    private String title;
    private String content;
}
