package org.multiverse.campusauction.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName auction_item
 */
@TableName(value ="auction_item")
@Data
public class AuctionItem {
    /**
     * 拍卖品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发布者
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 商品名称
     */
    @TableField(value = "title")
    private String title;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 起拍价
     */
    @TableField(value = "start_price")
    private BigDecimal startPrice;

    /**
     * 当前价
     */
    @TableField(value = "current_price")
    private BigDecimal currentPrice;

    /**
     * 加价幅度
     */
    @TableField(value = "step_price")
    private BigDecimal stepPrice;

    /**
     * 开拍时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 0草稿 1审核中 2审核通过 3竞拍中 4已成交 5流拍
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 审核意见
     */
    @TableField(value = "audit_comment")
    private String auditComment;

    /**
     * 封面
     */
    @TableField(value = "cover_image")
    private String coverImage;

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
     * 删除逻辑(0默认,1删除)
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    /**
     * 乐观锁(0默认)
     */
    @TableField(value = "version")
    private Long version;

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
        AuctionItem other = (AuctionItem) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getStartPrice() == null ? other.getStartPrice() == null : this.getStartPrice().equals(other.getStartPrice()))
            && (this.getCurrentPrice() == null ? other.getCurrentPrice() == null : this.getCurrentPrice().equals(other.getCurrentPrice()))
            && (this.getStepPrice() == null ? other.getStepPrice() == null : this.getStepPrice().equals(other.getStepPrice()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getAuditComment() == null ? other.getAuditComment() == null : this.getAuditComment().equals(other.getAuditComment()))
            && (this.getCoverImage() == null ? other.getCoverImage() == null : this.getCoverImage().equals(other.getCoverImage()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getStartPrice() == null) ? 0 : getStartPrice().hashCode());
        result = prime * result + ((getCurrentPrice() == null) ? 0 : getCurrentPrice().hashCode());
        result = prime * result + ((getStepPrice() == null) ? 0 : getStepPrice().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getAuditComment() == null) ? 0 : getAuditComment().hashCode());
        result = prime * result + ((getCoverImage() == null) ? 0 : getCoverImage().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", title=").append(title);
        sb.append(", description=").append(description);
        sb.append(", startPrice=").append(startPrice);
        sb.append(", currentPrice=").append(currentPrice);
        sb.append(", stepPrice=").append(stepPrice);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", status=").append(status);
        sb.append(", auditComment=").append(auditComment);
        sb.append(", coverImage=").append(coverImage);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", version=").append(version);
        sb.append("]");
        return sb.toString();
    }
}