package org.multiverse.campusauction.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.multiverse.campusauction.entity.domain.OrderInfo;

@Data
public class OrderInfoVo extends OrderInfo {

    /**
     * 拍卖品
     */
    private String itemName;

    /**
     * 卖家ID
     */

    private String sellerUserName;
}
