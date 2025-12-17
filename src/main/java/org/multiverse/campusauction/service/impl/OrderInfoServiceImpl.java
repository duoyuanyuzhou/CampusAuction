package org.multiverse.campusauction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.multiverse.campusauction.entity.domain.AuctionItem;
import org.multiverse.campusauction.entity.domain.BidRecord;
import org.multiverse.campusauction.entity.domain.OrderInfo;
import org.multiverse.campusauction.entity.domain.User;
import org.multiverse.campusauction.entity.vo.OrderInfoVo;
import org.multiverse.campusauction.notification.consumer.NotificationConsumer;
import org.multiverse.campusauction.notification.event.NotificationEvent;
import org.multiverse.campusauction.service.AuctionItemService;
import org.multiverse.campusauction.service.BidRecordService;
import org.multiverse.campusauction.service.OrderInfoService;
import org.multiverse.campusauction.mapper.OrderInfoMapper;
import org.multiverse.campusauction.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author jiaxi
* @description 针对表【order_info(订单表)】的数据库操作Service实现
* @createDate 2025-11-29 17:28:16
*/
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
    implements OrderInfoService{
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private AuctionItemService  auctionItemService;
    @Autowired
    private BidRecordService bidRecordService;

    @Autowired
    private UserService userService;

    @Resource
    private NotificationConsumer notificationConsumer;

    @Transactional
    @Override
    public OrderInfo creaateOrderInfo(Long itemId) {
        AuctionItem auctionItem = auctionItemService.getById(itemId);

        BidRecord highestBid = bidRecordService.getOne(
                new LambdaQueryWrapper<BidRecord>()
                        .eq(BidRecord::getItemId, itemId)
                        .orderByDesc(BidRecord::getBidPrice)
                        .orderByAsc(BidRecord::getCreateTime)
                        .last("LIMIT 1")
        );
        LambdaUpdateWrapper<AuctionItem> auctionItemLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if(highestBid==null){
            auctionItemLambdaUpdateWrapper.set(AuctionItem::getStatus,5);

        }
        auctionItemLambdaUpdateWrapper.set(AuctionItem::getStatus,4)
                .set(AuctionItem::getCurrentPrice, highestBid.getBidPrice());
        auctionItemService.update(auctionItemLambdaUpdateWrapper);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setItemId(itemId);
        orderInfo.setBuyerId(highestBid.getUserId());
        orderInfo.setSellerId(auctionItem.getUserId());
        orderInfo.setFinalPrice(highestBid.getBidPrice());
        orderInfo.setStatus(0);
        orderInfoMapper.insert(orderInfo);
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setUserId(orderInfo.getBuyerId());
        notificationEvent.setType("AUCTION_WIN");
        notificationEvent.setContent("竞拍成功，订单已生成");
        notificationEvent.setTitle("竞拍成功");
        notificationEvent.setBizId(1);
        notificationConsumer.consume(notificationEvent);
        return orderInfo;
    }

    @Transactional
    @Override
    public List<OrderInfo> creaateOrderInfoList(List<Long> itemIds) {
        List<AuctionItem> items = auctionItemService.listByIds(itemIds);

        List<BidRecord> bids = bidRecordService.list(
                new LambdaQueryWrapper<BidRecord>().in(BidRecord::getItemId, itemIds)
        );

        // 按 itemId 找到最高出价
        Map<Long, BidRecord> highestBidMap = bids.stream()
                .collect(Collectors.groupingBy(
                        BidRecord::getItemId,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(BidRecord::getBidPrice)
                                        .thenComparing(BidRecord::getCreateTime)),
                                Optional::get
                        )
                ));

        List<OrderInfo> orderInfos = new ArrayList<>();

        for (AuctionItem item : items) {
            BidRecord highestBid = highestBidMap.get(item.getId());

            LambdaUpdateWrapper<AuctionItem> updateWrapper = new LambdaUpdateWrapper<>();
            if (highestBid == null) {
                updateWrapper.set(AuctionItem::getStatus, 5); // 流拍
            } else {
                updateWrapper.set(AuctionItem::getStatus, 4)
                        .set(AuctionItem::getCurrentPrice, highestBid.getBidPrice());
            }
            auctionItemService.update(updateWrapper);

            if (highestBid != null) {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setItemId(item.getId());
                orderInfo.setBuyerId(highestBid.getUserId());
                orderInfo.setSellerId(item.getUserId());
                orderInfo.setFinalPrice(highestBid.getBidPrice());
                orderInfo.setStatus(0);
                orderInfos.add(orderInfo);
            }
        }

        // 批量插入
        if (!orderInfos.isEmpty()) {
            this.saveBatch(orderInfos);
        }

        return orderInfos;
    }


    @Override
    public Page<OrderInfoVo> getOrderInfoPage(Page<OrderInfo> page, OrderInfo orderInfo) {
        // 1️⃣ 查询分页订单
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getBuyerId, orderInfo.getBuyerId());
        Page<OrderInfo> orderInfoPage = this.baseMapper.selectPage(page, queryWrapper);
        List<OrderInfo> records = orderInfoPage.getRecords();

        if (records.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0); // 没有记录直接返回空分页
        }

        // 2️⃣ 查询对应商品信息
        List<Long> itemIds = records.stream()
                .map(OrderInfo::getItemId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        List<AuctionItem> auctionItemList = auctionItemService.list(
                new LambdaQueryWrapper<AuctionItem>().in(AuctionItem::getId, itemIds)
        );
        Map<Long, AuctionItem> auctionItemMap = auctionItemList.stream()
                .collect(Collectors.toMap(AuctionItem::getId, item -> item));

        // 3️⃣ 查询买家和卖家信息
        Set<Long> allUserIds = records.stream()
                .flatMap(order -> Stream.of(order.getBuyerId(), order.getSellerId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<User> userList = userService.list(
                new LambdaQueryWrapper<User>().in(User::getId, allUserIds)
        );
        Map<Long, User> userMap = userList.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // 4️⃣ 组装 OrderInfoVo
        List<OrderInfoVo> orderInfoVos = records.stream().map(orderInfo1 -> {
            OrderInfoVo vo = new OrderInfoVo();
            BeanUtils.copyProperties(orderInfo1, vo);



            // 设置卖家名称
            User seller = userMap.get(orderInfo1.getSellerId());
            if (seller != null) {
                vo.setSellerUserName(seller.getUsername());
            }

            // 设置商品名称
            AuctionItem item = auctionItemMap.get(orderInfo1.getItemId());
            if (item != null) {
                vo.setItemName(item.getTitle());
            }

            return vo;
        }).collect(Collectors.toList());

        // 5️⃣ 返回分页数据，替换原记录为 Vo
        Page<OrderInfoVo> resultPage = new Page<>(orderInfoPage.getCurrent(), orderInfoPage.getSize(), orderInfoPage.getTotal());
        resultPage.setRecords(orderInfoVos);

        return resultPage;
    }

}




