package org.multiverse.campusauction.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 拍卖延时规则表
 * @TableName auction_delay_config
 */
@TableName(value ="auction_delay_config")
@Data
public class AuctionDelayConfig {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 每次延迟秒数（如30秒）
     */
    @TableField(value = "delay_seconds")
    private Integer delaySeconds;

    /**
     * 若结束前多少秒内出价触发延时
     */
    @TableField(value = "threshold_seconds")
    private Integer thresholdSeconds;

    /**
     * 创建时间
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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AuctionDelayConfig other = (AuctionDelayConfig) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDelaySeconds() == null ? other.getDelaySeconds() == null : this.getDelaySeconds().equals(other.getDelaySeconds()))
            && (this.getThresholdSeconds() == null ? other.getThresholdSeconds() == null : this.getThresholdSeconds().equals(other.getThresholdSeconds()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDelaySeconds() == null) ? 0 : getDelaySeconds().hashCode());
        result = prime * result + ((getThresholdSeconds() == null) ? 0 : getThresholdSeconds().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", delaySeconds=").append(delaySeconds);
        sb.append(", thresholdSeconds=").append(thresholdSeconds);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", delFlag=").append(delFlag);
        sb.append("]");
        return sb.toString();
    }
}