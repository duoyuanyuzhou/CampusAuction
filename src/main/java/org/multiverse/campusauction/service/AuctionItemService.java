package org.multiverse.campusauction.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.multiverse.campusauction.entity.domain.AuctionItem;

/**
* @author jiaxi
* @description 针对表【auction_item】的数据库操作Service
* @createDate 2025-11-29 17:28:16
*/
public interface AuctionItemService extends IService<AuctionItem> {

    public Page<AuctionItem> getAuctionItemsPage(int page, int size, AuctionItem auctionItem, Long userId);

    public Page<AuctionItem> getAuctionItemsPageByAdmin(int page, int size, AuctionItem auctionItem);

    public Page<AuctionItem> getPageByUser(int page, int size, AuctionItem auctionItem, Long userId);

}
