package org.multiverse.campusauction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.multiverse.campusauction.entity.domain.AuctionItem;
import org.multiverse.campusauction.service.AuctionItemService;
import org.multiverse.campusauction.mapper.AuctionItemMapper;
import org.springframework.stereotype.Service;

/**
* @author jiaxi
* @description 针对表【auction_item】的数据库操作Service实现
* @createDate 2025-11-29 17:28:16
*/
@Service
public class AuctionItemServiceImpl extends ServiceImpl<AuctionItemMapper, AuctionItem>
    implements AuctionItemService{
    @Override
    public Page<AuctionItem> getAuctionItemsPage(int page, int size, AuctionItem auctionItem,  Long userId) {
        Page<AuctionItem> auctionItemPage = new Page<>(page, size);
        LambdaQueryWrapper<AuctionItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AuctionItem::getStatus, 2,3);
        queryWrapper.like(auctionItem.getTitle() != null, AuctionItem::getTitle, auctionItem.getTitle());
        queryWrapper.ge(
                auctionItem.getStartTime() != null,
                AuctionItem::getStartTime,
                auctionItem.getStartTime()
        );

        queryWrapper.le(
                auctionItem.getEndTime() != null,
                AuctionItem::getEndTime,
                auctionItem.getEndTime()
        );

        if (userId != null) {
            queryWrapper.eq(AuctionItem::getUserId, userId);
        }
        queryWrapper.orderByDesc(AuctionItem::getCreateTime);
        Page<AuctionItem> result = this.baseMapper.selectPage(auctionItemPage, queryWrapper);
        return result;
    }

    @Override
    public Page<AuctionItem> getAuctionItemsPageByAdmin(int page, int size, AuctionItem auctionItem) {
        Page<AuctionItem> auctionItemPage = new Page<>(page, size);
        LambdaQueryWrapper<AuctionItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AuctionItem::getStatus, 1,2);
        queryWrapper.ge(
                auctionItem.getStartTime() != null,
                AuctionItem::getStartTime,
                auctionItem.getStartTime()
        );

        queryWrapper.le(
                auctionItem.getEndTime() != null,
                AuctionItem::getEndTime,
                auctionItem.getEndTime()
        );
        Page<AuctionItem> result = this.baseMapper.selectPage(auctionItemPage, queryWrapper);
        return result;
    }

    @Override
    public Page<AuctionItem> getPageByUser(int page, int size, AuctionItem auctionItem, Long userId) {
        Page<AuctionItem> auctionItemPage = new Page<>(page, size);
        LambdaQueryWrapper<AuctionItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(auctionItem.getTitle() != null, AuctionItem::getTitle, auctionItem.getTitle());
        queryWrapper.ge(
                auctionItem.getStartTime() != null,
                AuctionItem::getStartTime,
                auctionItem.getStartTime()
        );

        queryWrapper.le(
                auctionItem.getEndTime() != null,
                AuctionItem::getEndTime,
                auctionItem.getEndTime()
        );

        if (userId != null) {
            queryWrapper.eq(AuctionItem::getUserId, userId);
        }
        queryWrapper.orderByDesc(AuctionItem::getCreateTime);
        Page<AuctionItem> result = this.baseMapper.selectPage(auctionItemPage, queryWrapper);
        return result;
    }
}




