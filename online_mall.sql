/*
Navicat MySQL Data Transfer

Source Server         : kk
Source Server Version : 80011
Source Host           : localhost:3306
Source Database       : online_mall

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2018-11-19 17:27:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mmall_cart
-- ----------------------------
DROP TABLE IF EXISTS `mmall_cart`;
CREATE TABLE `mmall_cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '对应商品所属的用户id',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `quantity` int(11) DEFAULT NULL COMMENT '数量',
  `checked` int(11) DEFAULT NULL COMMENT '是否选择,1=已选,0=未选',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id_index` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mmall_cart
-- ----------------------------
INSERT INTO `mmall_cart` VALUES ('125', '2', '27', '20', null, '2018-11-12 15:00:38', '2018-11-12 15:00:38');

-- ----------------------------
-- Table structure for mmall_category
-- ----------------------------
DROP TABLE IF EXISTS `mmall_category`;
CREATE TABLE `mmall_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类别id',
  `parent_id` int(11) DEFAULT NULL COMMENT '父类别id,id=0时说明是根节点',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类别名称',
  `status` tinyint(1) DEFAULT '1' COMMENT '类别状态 1-正常,2-已废弃',
  `sort_order` int(4) DEFAULT NULL COMMENT '排序编号,同类展示顺序,数值相等则自然排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100038 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mmall_category
-- ----------------------------
INSERT INTO `mmall_category` VALUES ('100034', '0', '生活类', '1', null, '2018-11-05 16:19:37', '2018-11-05 16:33:03');
INSERT INTO `mmall_category` VALUES ('100035', '100034', '母婴用品', '1', null, '2018-11-05 17:09:36', '2018-11-05 17:09:36');
INSERT INTO `mmall_category` VALUES ('100036', '0', '电子数码类', '1', null, '2018-11-05 17:09:46', '2018-11-05 17:09:46');
INSERT INTO `mmall_category` VALUES ('100037', '100036', '手机', '1', null, '2018-11-05 17:35:16', '2018-11-05 17:35:16');

-- ----------------------------
-- Table structure for mmall_order
-- ----------------------------
DROP TABLE IF EXISTS `mmall_order`;
CREATE TABLE `mmall_order` (
  `order_no` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单号',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `shipping_id` int(11) DEFAULT NULL COMMENT '订单地址',
  `payment` decimal(20,2) DEFAULT NULL COMMENT '实际支付金额,元',
  `payment_type` int(4) DEFAULT NULL COMMENT '支付类型 1-在线支付',
  `postage` int(10) DEFAULT NULL COMMENT '运费,元',
  `status` int(10) DEFAULT NULL COMMENT '订单状态 0-已取消,10-未付款,20-已付款,40-已发货,50-交易成功,60-交易关闭',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `send_time` datetime DEFAULT NULL COMMENT '发货时间',
  `end_time` datetime DEFAULT NULL COMMENT '交易完成时间',
  `close_time` datetime DEFAULT NULL COMMENT '交易关闭时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`order_no`),
  UNIQUE KEY `uk_order_no` (`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mmall_order
-- ----------------------------
INSERT INTO `mmall_order` VALUES ('4', '2', '33', '7999.98', null, null, '10', null, null, null, null, '2018-11-19 14:37:47', '2018-11-19 14:37:47');
INSERT INTO `mmall_order` VALUES ('5', '2', '33', '19999.95', null, null, '50', '2018-11-19 17:21:49', '2018-11-19 17:23:04', '2018-11-19 17:23:55', null, '2018-11-19 15:04:16', '2018-11-19 17:22:41');
INSERT INTO `mmall_order` VALUES ('6', '2', '33', '17998.00', null, null, '10', null, null, null, null, '2018-11-19 15:04:16', '2018-11-19 15:04:16');
INSERT INTO `mmall_order` VALUES ('7', '22', '35', '1600.00', null, null, '10', null, null, null, null, '2018-11-19 16:55:42', '2018-11-19 16:55:42');

-- ----------------------------
-- Table structure for mmall_order_info
-- ----------------------------
DROP TABLE IF EXISTS `mmall_order_info`;
CREATE TABLE `mmall_order_info` (
  `order_no` bigint(20) NOT NULL COMMENT '订单编号',
  `pay_type` int(10) NOT NULL COMMENT '支付平台:1-支付宝,2-微信',
  `out_trade_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付宝流水号',
  `n` int(11) NOT NULL COMMENT '订单编号索引(每次更新时自增)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`order_no`),
  KEY `k_alipay_trade_no` (`out_trade_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mmall_order_info
-- ----------------------------
INSERT INTO `mmall_order_info` VALUES ('5', '1', '5_1', '1', '2018-11-19 17:19:43', '2018-11-19 17:21:09');

-- ----------------------------
-- Table structure for mmall_order_item
-- ----------------------------
DROP TABLE IF EXISTS `mmall_order_item`;
CREATE TABLE `mmall_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单子表id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `order_no` bigint(20) DEFAULT NULL COMMENT '订单编号',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `product_main_image` varchar(500) DEFAULT NULL COMMENT '商品主图片url',
  `current_unit_price` decimal(20,2) DEFAULT NULL COMMENT '生成订单时的商品单价,元',
  `quantity` int(10) DEFAULT NULL COMMENT '商品数量',
  `total_price` decimal(20,2) DEFAULT NULL COMMENT '商品总价,元',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_no` (`order_no`) USING BTREE,
  KEY `idx_order_no_user_id` (`user_id`,`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mmall_order_item
-- ----------------------------
INSERT INTO `mmall_order_item` VALUES ('113', '2', '4', '28', '小米mix3', '11.jpg', '3999.99', '2', '7999.98', '2018-11-19 14:37:47', '2018-11-19 14:37:47');
INSERT INTO `mmall_order_item` VALUES ('114', '2', '5', '28', '小米mix3', '11.jpg', '3999.99', '5', '19999.95', '2018-11-19 15:04:16', '2018-11-19 15:04:16');
INSERT INTO `mmall_order_item` VALUES ('115', '2', '6', '29', 'apple iphoneX', null, '8999.00', '2', '17998.00', '2018-11-19 15:04:16', '2018-11-19 15:04:16');
INSERT INTO `mmall_order_item` VALUES ('116', '22', '7', '30', '惠氏婴幼儿奶粉1段', 'huishi.jpg', '160.00', '10', '1600.00', '2018-11-19 16:55:42', '2018-11-19 16:55:42');

-- ----------------------------
-- Table structure for mmall_product
-- ----------------------------
DROP TABLE IF EXISTS `mmall_product`;
CREATE TABLE `mmall_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `category_id` int(11) NOT NULL COMMENT '分类id,对应mmall_category表的主键',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `subtitle` varchar(200) DEFAULT NULL COMMENT '商品副标题',
  `main_image` varchar(500) DEFAULT NULL COMMENT '商品主图url地址',
  `sub_images` text COMMENT '图片地址,json格式,扩展用',
  `detail` text COMMENT '商品详情',
  `price` decimal(20,2) NOT NULL COMMENT '价格,元',
  `stock` int(11) NOT NULL COMMENT '库存数量',
  `status` int(6) DEFAULT '1' COMMENT '商品状态 1-在售,2-下架,3-删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mmall_product
-- ----------------------------
INSERT INTO `mmall_product` VALUES ('27', '100035', '小萌希奥纸尿裤', null, '333.jpg', '333.jpg', null, '198.64', '103', '1', '2018-11-06 10:51:59', '2018-11-06 14:08:13');
INSERT INTO `mmall_product` VALUES ('28', '100036', '小米mix3', '小米手机', '11.jpg', '11.jpg,22.jpg', null, '3999.99', '45', '1', '2018-11-06 14:51:12', '2018-11-19 15:04:16');
INSERT INTO `mmall_product` VALUES ('29', '100037', 'apple iphoneX', '苹果手机', null, null, null, '8999.00', '18', '1', '2018-11-06 15:14:30', '2018-11-19 15:04:16');
INSERT INTO `mmall_product` VALUES ('30', '100035', '惠氏婴幼儿奶粉1段', '爱尔兰进口', 'huishi.jpg', 'huishi.jpg', '0-6个月', '160.00', '1990', '1', '2018-11-19 16:51:34', '2018-11-19 16:55:42');

-- ----------------------------
-- Table structure for mmall_shipping
-- ----------------------------
DROP TABLE IF EXISTS `mmall_shipping`;
CREATE TABLE `mmall_shipping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `receiver_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货固定电话',
  `receiver_mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货移动电话',
  `receiver_province` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '省份',
  `receiver_city` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '城市',
  `receiver_district` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区/县',
  `receiver_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '详细地址',
  `receiver_zip` varchar(6) DEFAULT NULL COMMENT '邮编',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id_index` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mmall_shipping
-- ----------------------------
INSERT INTO `mmall_shipping` VALUES ('33', '2', '邱先生', null, '18695891383', '河南', '郑州', '郑东新区', '商鼎路东风南路升龙广场3号楼A座29楼魔飞公寓', null, '2018-11-13 09:59:26', '2018-11-13 09:59:26');
INSERT INTO `mmall_shipping` VALUES ('35', '22', '邱先生', null, '18695891383', '河南', '郑州', '管城回族区', '商鼎路东风南路升龙广场3号楼A座29楼魔飞公寓', null, '2018-11-13 10:53:06', '2018-11-13 10:56:03');

-- ----------------------------
-- Table structure for mmall_user
-- ----------------------------
DROP TABLE IF EXISTS `mmall_user`;
CREATE TABLE `mmall_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '用户密码,MD5',
  `email` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '用户手机号',
  `pw_question` varchar(100) DEFAULT NULL COMMENT '找回密码问题',
  `pw_answer` varchar(100) DEFAULT NULL COMMENT '找回密码答案',
  `role` int(4) NOT NULL COMMENT '角色 0-管理员 1-普通用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_unique` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mmall_user
-- ----------------------------
INSERT INTO `mmall_user` VALUES ('2', 'admin', 'f76de0d6d93cd5c9b1632041e6394c53', 'admin@shinvi.com', '133333333', '你是谁', '王力宏', '0', '2018-11-02 13:24:02', '2018-11-05 11:39:09');
INSERT INTO `mmall_user` VALUES ('22', '御坂美琴', '2ce89db8967fa0ff861b130d1df57223', 'misaka@shinvi.com', '166666666', '我的初中是哪一所', '常盘台', '1', '2018-11-05 10:55:56', '2018-11-06 14:13:13');

-- ----------------------------
-- Table structure for mmall_user_token
-- ----------------------------
DROP TABLE IF EXISTS `mmall_user_token`;
CREATE TABLE `mmall_user_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `token` varchar(200) NOT NULL COMMENT 'token',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_token` (`token`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mmall_user_token
-- ----------------------------
INSERT INTO `mmall_user_token` VALUES ('24', '2', '2018-12-19 16:57:23', '2a665858d7520002c13f6ec1813375f5', '2018-11-02 14:11:05', '2018-11-19 16:57:23');
INSERT INTO `mmall_user_token` VALUES ('25', '22', '2018-12-06 14:13:22', 'e7d5224c5007d8d635cbc69b89f131e8', '2018-11-06 14:13:22', '2018-11-06 14:13:22');
