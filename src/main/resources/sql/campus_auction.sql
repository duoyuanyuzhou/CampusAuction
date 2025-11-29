/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : campus_auction

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 29/11/2025 17:24:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auction_delay_config
-- ----------------------------
DROP TABLE IF EXISTS `auction_delay_config`;
CREATE TABLE `auction_delay_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `delay_seconds` int NOT NULL DEFAULT 0 COMMENT '每次延迟秒数（如30秒）',
  `threshold_seconds` int NOT NULL DEFAULT 0 COMMENT '若结束前多少秒内出价触发延时',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0默认,1删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '拍卖延时规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auction_delay_config
-- ----------------------------

-- ----------------------------
-- Table structure for auction_item
-- ----------------------------
DROP TABLE IF EXISTS `auction_item`;
CREATE TABLE `auction_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '拍卖品ID',
  `user_id` bigint NOT NULL COMMENT '发布者',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '描述',
  `start_price` decimal(10, 2) NOT NULL COMMENT '起拍价',
  `current_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '当前价',
  `step_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '加价幅度',
  `start_time` datetime NOT NULL COMMENT '开拍时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint NOT NULL COMMENT '0草稿 1审核中 2审核通过 3竞拍中 4已成交 5流拍',
  `audit_comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核意见',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封面',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint NOT NULL DEFAULT 0 COMMENT '删除逻辑(0默认,1删除)',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '乐观锁(0默认)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auction_item
-- ----------------------------

-- ----------------------------
-- Table structure for auction_item_image
-- ----------------------------
DROP TABLE IF EXISTS `auction_item_image`;
CREATE TABLE `auction_item_image`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `item_id` bigint NOT NULL COMMENT '关联商品ID',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片URL',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0默认,1删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auction_item_image
-- ----------------------------

-- ----------------------------
-- Table structure for bid_record
-- ----------------------------
DROP TABLE IF EXISTS `bid_record`;
CREATE TABLE `bid_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `item_id` bigint NOT NULL COMMENT '拍卖品ID',
  `user_id` bigint NOT NULL COMMENT '出价者ID',
  `bid_price` decimal(10, 2) NOT NULL COMMENT '出价金额',
  `bid_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '出价时间',
  `is_win` tinyint NOT NULL DEFAULT 0 COMMENT '是否最终中标 0=否 1=是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0默认,1删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_item_bid_time`(`item_id` ASC, `bid_time` ASC) USING BTREE,
  INDEX `idx_item_bid_price`(`item_id` ASC, `bid_price` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '竞价记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bid_record
-- ----------------------------

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `item_id` bigint NOT NULL COMMENT '拍卖品ID',
  `buyer_id` bigint NOT NULL COMMENT '买家ID',
  `seller_id` bigint NOT NULL COMMENT '卖家ID',
  `final_price` decimal(10, 2) NOT NULL COMMENT '成交价',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '订单状态 0=待支付 1=已支付 2=已取消 3=已完成',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0默认,1删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_item_status`(`item_id` ASC, `status` ASC) USING BTREE,
  INDEX `idx_buyer_status`(`buyer_id` ASC, `status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_info
-- ----------------------------

-- ----------------------------
-- Table structure for payment_record
-- ----------------------------
DROP TABLE IF EXISTS `payment_record`;
CREATE TABLE `payment_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '关联订单ID',
  `amount` decimal(10, 2) NOT NULL COMMENT '支付金额',
  `pay_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式，如支付宝/微信/银行卡',
  `pay_status` tinyint NOT NULL DEFAULT 0 COMMENT '支付状态 0=未支付 1=已支付 2=支付失败',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0默认,1删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_paystatus`(`order_id` ASC, `pay_status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_record
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码（加密）',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `school_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学号（可选）',
  `role` tinyint NOT NULL DEFAULT 0 COMMENT '1=学生 2=管理员',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '0=禁用 1=正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0默认,1删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for user_message
-- ----------------------------
DROP TABLE IF EXISTS `user_message`;
CREATE TABLE `user_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '接收用户ID',
  `type` tinyint NOT NULL DEFAULT 0 COMMENT '消息类型，如0系统消息 1竞拍消息',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容',
  `is_read` tinyint NOT NULL DEFAULT 0 COMMENT '是否已读 0=未读 1=已读',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0默认,1删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_isread`(`user_id` ASC, `is_read` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_message
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
