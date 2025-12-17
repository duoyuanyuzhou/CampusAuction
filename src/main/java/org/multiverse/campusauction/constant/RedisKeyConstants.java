package org.multiverse.campusauction.constant;

/**
 * Redis Key 常量
 */
public final class RedisKeyConstants {

    private RedisKeyConstants() {}

    /** 拍卖自动延时次数 */
    public static final String AUCTION_EXTEND_COUNT = "auction:extend:count:";

    /** 拍卖结束延时队列 key（示例） */
    public static final String AUCTION_END_DELAY = "auction:end:delay:";

    /** 拍卖商品队列 */
    public static final String AUCTION_AUDIT_DELAY = "auction:audit:delay:";

    // 拍卖详情缓存
    public static final String AUCTION_ITEM_CACHE = "auction:item:";



}
