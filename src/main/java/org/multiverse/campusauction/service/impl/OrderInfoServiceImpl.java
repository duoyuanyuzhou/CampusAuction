package org.multiverse.campusauction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.multiverse.campusauction.entity.domain.OrderInfo;
import org.multiverse.campusauction.service.OrderInfoService;
import org.multiverse.campusauction.mapper.OrderInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author jiaxi
* @description 针对表【order_info(订单表)】的数据库操作Service实现
* @createDate 2025-11-29 17:28:16
*/
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
    implements OrderInfoService{

}




