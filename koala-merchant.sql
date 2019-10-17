/*
SQLyog Ultimate v12.2.6 (64 bit)
MySQL - 8.0.17 : Database - koala-merchant
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`koala-merchant` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `koala-merchant`;

/*Table structure for table `member` */

DROP TABLE IF EXISTS `member`;

CREATE TABLE `member` (
  `ID` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `UNION_ID` varchar(50) NOT NULL COMMENT '微信开放平台关联ID',
  `MERCHANT_ID` bigint(64) NOT NULL COMMENT '商户ID',
  `MERCHANT_NO` varchar(50) NOT NULL COMMENT '商户编号',
  `MEMBER_NO` varchar(50) NOT NULL COMMENT '会员编号',
  `MEMBER_NAME` varchar(50) DEFAULT NULL COMMENT '会员名称',
  `MEMBER_NICK_NAME` varchar(50) DEFAULT NULL COMMENT '会员昵称',
  `SEX` char(1) DEFAULT NULL COMMENT '会员性别：M-男 F-女 N-未知',
  `BIRTHDAY` date DEFAULT NULL COMMENT '出生日期',
  `MOBILE` varchar(12) DEFAULT NULL COMMENT '手机号',
  `CREATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `MEMBER_MEMBER_NO_UINDEX` (`MEMBER_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员信息表';

/*Data for the table `member` */

/*Table structure for table `member_card` */

DROP TABLE IF EXISTS `member_card`;

CREATE TABLE `member_card` (
  `ID` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MERCHANT_CARD_ID` bigint(64) NOT NULL COMMENT '会员卡号',
  `MERCHANT_ID` bigint(64) NOT NULL COMMENT '商户ID',
  `MERCHANT_NO` varchar(50) NOT NULL COMMENT '商户编号',
  `MEMBER_ID` bigint(64) NOT NULL COMMENT '会员ID',
  `MEMBER_NO` varchar(50) DEFAULT NULL COMMENT '会员编号',
  `WX_CARD_NO` varchar(50) NOT NULL COMMENT '领取微信会员卡编号',
  `CARD_TYPE` int(1) NOT NULL DEFAULT '1' COMMENT '领取卡类型：1-会员卡 2-团购券 3-折扣券 4-代金券 5-通用券 6-景点门票 7-电影票 8-机票',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员卡';

/*Data for the table `member_card` */

/*Table structure for table `merchant` */

DROP TABLE IF EXISTS `merchant`;

CREATE TABLE `merchant` (
  `ID` bigint(64) NOT NULL AUTO_INCREMENT,
  `MERCHANT_NO` varchar(50) NOT NULL COMMENT '商户编号',
  `MERCHANT_NAME` varchar(50) DEFAULT NULL COMMENT '商户名称',
  `MERCHANT_EMAIL` varchar(12) DEFAULT NULL COMMENT '商户邮箱',
  `MERCHANT_MOBILE` varchar(13) NOT NULL COMMENT '商户手机号',
  `MERCHANT_PARENT_ID` bigint(64) DEFAULT NULL COMMENT '上级商户',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `MERCHANT_ID_UINDEX` (`ID`),
  UNIQUE KEY `MERCHANT_MERCHANT_NO_UINDEX` (`MERCHANT_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户表';

/*Data for the table `merchant` */

/*Table structure for table `merchant_card` */

DROP TABLE IF EXISTS `merchant_card`;

CREATE TABLE `merchant_card` (
  `ID` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '商户卡ID',
  `MERCHANT_ID` bigint(64) NOT NULL COMMENT '商户ID',
  `MERCHANT_NO` varchar(50) NOT NULL COMMENT '商户编号',
  `CARD_NAME` varchar(50) NOT NULL COMMENT '会员卡名称',
  `CARD_SUB_NAME` varchar(50) NOT NULL COMMENT '会员卡副名称',
  `CARD_LOGO` varchar(125) NOT NULL COMMENT '会员卡LOGO',
  `CARD_BACK_URL` varchar(125) DEFAULT NULL COMMENT '会员卡背景图',
  `CARD_BACK_COLOR` varchar(50) NOT NULL COMMENT '会员卡颜色',
  `CARD_STATUS` int(1) NOT NULL DEFAULT '0' COMMENT '会员卡状态：-1删除 0正常 1失效',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户会员卡';

/*Data for the table `merchant_card` */

/*Table structure for table `merchant_card_bonus_rule` */

DROP TABLE IF EXISTS `merchant_card_bonus_rule`;

CREATE TABLE `merchant_card_bonus_rule` (
  `ID` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MERCHANT_CARD_ID` bigint(64) NOT NULL COMMENT '商户会员卡ID',
  `MERCHANT_NO` varchar(50) NOT NULL COMMENT '商户编号',
  `COST_MONEY` bigint(64) NOT NULL COMMENT '消费金额 以分为单位',
  `COST_INCR_BONUS` bigint(64) DEFAULT NULL COMMENT '对应消费增加的积分',
  `MAX_BONUS` bigint(64) NOT NULL COMMENT '用户单次可获取的积分上限',
  `COST_BONUS` bigint(64) DEFAULT NULL COMMENT '每使用5积分',
  `REDUCE_MONEY` bigint(64) NOT NULL COMMENT '抵扣xx元，（这里以分为单位）',
  `REDUCE_BONUS_FACTOR` bigint(64) NOT NULL COMMENT '抵扣条件，满xx元（这里以分为单位）可用',
  `MAX_REDUCE_BONUS` bigint(64) NOT NULL COMMENT '抵扣条件，单笔最多使用xx积分',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户会员卡积分规则';

/*Data for the table `merchant_card_bonus_rule` */

/*Table structure for table `merchant_card_customize` */

DROP TABLE IF EXISTS `merchant_card_customize`;

CREATE TABLE `merchant_card_customize` (
  `ID` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MERCHANT_CARD_ID` bigint(64) NOT NULL COMMENT '商户会员卡ID',
  `MERCHANT_NO` varchar(50) NOT NULL COMMENT '商户编号',
  `MAIN_NAME` varchar(50) NOT NULL COMMENT '入口名称',
  `MAIN_URL` varchar(200) DEFAULT NULL COMMENT '入口URL',
  `MAIN_NOTICE` varchar(50) DEFAULT NULL COMMENT '入口提示语',
  `MAIN_TYPE` int(1) DEFAULT NULL COMMENT '入口类型：1必填入口 2自定义 3营销入口',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户会员卡自定义入口';

/*Data for the table `merchant_card_customize` */

/*Table structure for table `merchant_card_expand` */

DROP TABLE IF EXISTS `merchant_card_expand`;

CREATE TABLE `merchant_card_expand` (
  `ID` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MERCHANT_CARD_ID` bigint(64) NOT NULL COMMENT '商户会员卡ID',
  `MERCHANT_NO` varchar(50) NOT NULL COMMENT '商户编号',
  `OPEN_METHOD` int(1) NOT NULL DEFAULT '1' COMMENT '开卡方式 1直接开卡 2预存开卡  3付费开卡',
  `OPEN_MONEY` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '开卡金额：默认0.00  开放方式是直接开卡，此字段无效',
  `OPEN_ATTR` varchar(50) NOT NULL DEFAULT '1,2' COMMENT '开卡属性 1姓名 2手机号 3兴趣 4生日 5性别 6邮箱',
  `CARD_NOTICE` varchar(50) NOT NULL COMMENT '会员卡提示：结账时出示会员卡',
  `CARD_DESCRIPTION` varchar(50) NOT NULL COMMENT '会员卡使用说明：会员卡须先领卡再使用',
  `CARD_PRIVILEGE` varchar(50) NOT NULL COMMENT '会员卡特权说明',
  `CARD_VALID_TYPE` int(1) DEFAULT NULL COMMENT '有效期：1永久有效 DATE_TYPE_PERMANENT  2固定范围有效DATE_TYPE_FIX_TIME_RANGE 3自领取日 DATE_TYPE_FIX_TERM (单位为天当天有效输入0)',
  `CARD_BEGIN` int(11) DEFAULT NULL COMMENT '有效日期开始时间  永久有效不填 2018-01-01',
  `CARD_END` int(11) DEFAULT NULL COMMENT '有效日期结束时间  永久有效不填 2018-12-31',
  `CARD_LIMIT_TIME` int(11) DEFAULT NULL COMMENT '可用时段 例：周一至周五 全天 限制类型枚举值：支持填入 MONDAY 周一 TUESDAY 周二 WEDNESDAY 周三 THURSDAY 周四 FRIDAY 周五 SATURDAY 周六 SUNDAY 周日',
  `CARD_LIMIT_BEGIN` varchar(10) DEFAULT NULL COMMENT '可用时段：开始时间10：00',
  `CARD_LIMIT_END` varchar(10) DEFAULT NULL COMMENT '可用时段：结束时间23：59',
  `ACTIVATE_TYPE` int(1) DEFAULT NULL COMMENT '激活类型： 1自动激活 AUTO_ACTIVATE 2一键激活 WX_ACTIVATE',
  `DISPLAY_POINT` int(1) DEFAULT NULL COMMENT '是否显示积分：true或false',
  `CARD_POINT_URL` varchar(125) DEFAULT NULL COMMENT '设置跳转外链查看积分详情地址',
  `CARD_SUPPORT_BALANCE` int(1) DEFAULT NULL COMMENT '是否支持储值：true或false',
  `CARD_BALANCE_URL` varchar(125) DEFAULT NULL COMMENT '设置跳转外链查看余额详情地址',
  `CARD_DISPLAY_FIELD` varchar(50) DEFAULT '1' COMMENT '栏位展示  1积分 POINT  2余额 BALANCE  3优惠券 COUPON 4等级 LEVEL (只能有三项)',
  `CARD_DISPLAY_BAR` int(1) NOT NULL DEFAULT '1' COMMENT '条码展示：1条形码(CODE_TYPE_BARCODE) 2二维码(CODE_TYPE_QRCODE) 3文本(CODE_TYPE_TEXT)',
  `CARD_CENTER_TYPE` int(1) NOT NULL DEFAULT '1' COMMENT '中心按钮类型：1会员支付 2微信支付 3自定义 4不展示',
  `CARD_CENTER_BUTTON` varchar(50) NOT NULL COMMENT '中心按钮文案：会员支付 微信支付 自定义支付',
  `CARD_CENTER_TIPS` varchar(50) NOT NULL COMMENT '中心按钮提示： 点击生成付款码',
  `CARD_LIMIT` int(1) NOT NULL DEFAULT '1' COMMENT '每人可领券的数量限制，建议会员卡每人限领一张',
  `SUPPORT_STORES` int(1) NOT NULL DEFAULT '0' COMMENT '是否支持所有门店：0否 1是',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户会员卡拓展表';

/*Data for the table `merchant_card_expand` */

/*Table structure for table `merchant_card_sku` */

DROP TABLE IF EXISTS `merchant_card_sku`;

CREATE TABLE `merchant_card_sku` (
  `ID` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MERCHANT_ID` bigint(64) NOT NULL COMMENT '商户ID',
  `MERCHANT_NO` varchar(50) NOT NULL COMMENT '商户编号',
  `MERCHANT_CARD_ID` bigint(64) NOT NULL COMMENT '商户会员卡ID',
  `TOTAL_AMOUNT` int(9) NOT NULL DEFAULT '1' COMMENT '库存总数量：默认为1,最大为100000000',
  `SURPLUS_AMOUNT` int(9) NOT NULL DEFAULT '1' COMMENT '剩余数量：默认为1,最大为100000000',
  `USE_AMOUNT` int(9) NOT NULL DEFAULT '0' COMMENT '已领取库存数量',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户会员卡库存表';

/*Data for the table `merchant_card_sku` */

/*Table structure for table `merchant_store` */

DROP TABLE IF EXISTS `merchant_store`;

CREATE TABLE `merchant_store` (
  `ID` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MERCHANT_ID` bigint(64) NOT NULL COMMENT '商户ID',
  `MERCHANT_NO` varchar(50) NOT NULL COMMENT '商户编号',
  `MERCHANT_SID` bigint(64) NOT NULL COMMENT '商户门店ID',
  `MERCHANT_POI_ID` bigint(64) NOT NULL COMMENT '创建门店后微信返回的POI_ID 用作查询门店是否审核通过',
  `MERCHANT_STORE_NAME` varchar(50) NOT NULL COMMENT '门店名称',
  `MERCHANT_SUB_STORE_NAME` varchar(50) NOT NULL COMMENT '分店名称',
  `PROVINCE` varchar(20) NOT NULL COMMENT '省',
  `CITY` varchar(20) NOT NULL COMMENT '市',
  `DISTRICT` varchar(20) NOT NULL COMMENT '区',
  `STREET` varchar(50) NOT NULL COMMENT '详细地址：街道信息',
  `MERCHANT_STORE_MOBILE` varchar(13) NOT NULL COMMENT '门店电话',
  `MERCHANT_STORE_CLAZZ` varchar(100) NOT NULL COMMENT '门店类型(数组)：不同级分类用“,”隔开',
  `MERCHANT_STORE_LOCATION` varchar(50) NOT NULL COMMENT '门店坐标',
  `OFFSET_TYPE` int(1) NOT NULL DEFAULT '1' COMMENT '坐标类型： 1 为火星坐标 2 为sogou经纬度 3 为百度经纬度 4 为mapbar经纬度 5 为GPS坐标 6 为sogou墨卡托坐标 注：高德经纬度无需转换可直接使用',
  `MERCHANT_STORE_IMAGE` varchar(500) DEFAULT NULL COMMENT '门店图片列表（json格式）',
  `MERCHANT_STORE_OPEN_TIME` varchar(20) DEFAULT NULL COMMENT '营业时间，24 小时制表示，用“-”连接，如 8:00-20:00',
  `MERCHANT_STORE_RECOMMEND` varchar(200) DEFAULT NULL COMMENT '推荐品',
  `MERCHANT_STORE_SPECIAL` varchar(200) DEFAULT NULL COMMENT '特色服务',
  `MERCHANT_STORE_DESCRIPTION` varchar(300) DEFAULT NULL COMMENT '商户简介',
  `MERCHANT_STORE_PRICE` int(11) DEFAULT '1' COMMENT '人均价格',
  `MERCHANT_STORE_PARENT_SID` bigint(64) DEFAULT NULL COMMENT '上级门店SID',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `MERCHANT_STORE_MERCHANT_POI_ID_UINDEX` (`MERCHANT_POI_ID`),
  UNIQUE KEY `MERCHANT_STORE_MERCHANT_SID_UINDEX` (`MERCHANT_SID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户门店表';

/*Data for the table `merchant_store` */

/*Table structure for table `payment` */

DROP TABLE IF EXISTS `payment`;

CREATE TABLE `payment` (
  `ID` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `OPEN_ID` varchar(64) NOT NULL COMMENT '用户OPEN_ID',
  `PREPAY_ID` varchar(64) NOT NULL COMMENT '预付款ID',
  `ORDER_NO` varchar(64) NOT NULL COMMENT '订单编号',
  `TRADE_NO` varchar(64) NOT NULL COMMENT '交易单号',
  `PAYMENT_NO` varchar(64) DEFAULT NULL COMMENT '微信支付编号',
  `PAY_FEE` int(9) DEFAULT '0' COMMENT '支付金额(单位：分)',
  `PAY_STATUS` int(1) DEFAULT '0' COMMENT '支付状态：0-未支付  1-已支付  2-已退款',
  `PAY_METHOD` int(1) DEFAULT '1' COMMENT '支付方式：1-微信 2-支付宝 3-银联',
  `PAY_TIME` int(20) DEFAULT NULL COMMENT '支付完成时间',
  `CREATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付记录表';

/*Data for the table `payment` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
