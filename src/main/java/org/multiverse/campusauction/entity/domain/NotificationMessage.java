package org.multiverse.campusauction.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 站内通知消息表
 * @TableName notification_message
 */
@TableName(value ="notification_message")
@Data
public class NotificationMessage implements Serializable {
    /**
     * 消息ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接收用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 消息标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 消息内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 消息类型/模板code
     */
    @TableField(value = "type")
    private String type;

    /**
     * 业务类型 auction/order
     */
    @TableField(value = "biz_type")
    private String bizType;

    /**
     * 业务ID
     */
    @TableField(value = "biz_id")
    private Long bizId;

    /**
     * 是否已读 0未读 1已读
     */
    @TableField(value = "is_read")
    private Integer isRead;

    /**
     * 通知渠道 1站内信
     */
    @TableField(value = "channel")
    private Integer channel;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 阅读时间
     */
    @TableField(value = "read_time")
    private Date readTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}