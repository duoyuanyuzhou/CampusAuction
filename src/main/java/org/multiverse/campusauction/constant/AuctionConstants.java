package org.multiverse.campusauction.constant;

/**
 * 拍卖业务相关常量
 */
public final class AuctionConstants {

    private AuctionConstants() {}

    /** 自动延时触发阈值（秒） */
    public static final long AUTO_EXTEND_THRESHOLD_SECONDS = 30;

    /** 每次自动延长时间（秒） */
    public static final long AUTO_EXTEND_SECONDS = 30;

    /** 最大自动延时次数 */
    public static final int MAX_EXTEND_TIMES = 10;

    /** 自动延时默认最大拍卖时长（秒，可选） */
    public static final long MAX_AUCTION_DURATION_SECONDS = 2 * 60 * 60;
}
