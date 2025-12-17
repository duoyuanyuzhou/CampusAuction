package org.multiverse.campusauction.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.multiverse.campusauction.entity.domain.BidRecord;
import org.multiverse.campusauction.entity.domain.OrderInfo;
import org.multiverse.campusauction.entity.vo.OrderInfoVo;

import java.util.List;

/**
* @author jiaxi
* @description 针对表【order_info(订单表)】的数据库操作Service
* @createDate 2025-11-29 17:28:16
*/
public interface OrderInfoService extends IService<OrderInfo> {

    OrderInfo creaateOrderInfo(Long itemId);

    List<OrderInfo> creaateOrderInfoList(List<Long> itemIds);
    Page<OrderInfoVo> getOrderInfoPage(Page<OrderInfo> page, OrderInfo orderInfo);

}
