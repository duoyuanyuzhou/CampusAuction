package org.multiverse.campusauction.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户消息表
 * @TableName user_message
 */
@TableName(value ="user_message")
@Data
public class UserMessage implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接收用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 消息类型，如0系统消息 1竞拍消息
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 消息内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 是否已读 0=未读 1=已读
     */
    @TableField(value = "is_read")
    private Integer isRead;

    /**
     * 消息创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 删除标记(0默认,1删除)
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}