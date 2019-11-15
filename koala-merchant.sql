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
  `MERCHANT_CARD_ID` varchar(64) NOT NULL COMMENT '会员卡号',
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
  `MERCHANT_CARD_ID` varchar(64) NOT NULL COMMENT '会员卡ID',
  `WX_CARD_ID` varchar(64) DEFAULT NULL COMMENT '微信会员卡ID,未同步时为空',
  `BRAND_LOGO` varchar(125) NOT NULL COMMENT '品牌LOGO',
  `BRAND_NAME` varchar(50) NOT NULL COMMENT '品牌名称',
  `CARD_NAME` varchar(50) NOT NULL COMMENT '会员卡名称',
  `CARD_BACK_URL` varchar(125) DEFAULT NULL COMMENT '会员卡背景图',
  `CARD_BACK_COLOR` varchar(50) NOT NULL COMMENT '会员卡颜色',
  `DISCOUNT` int(5) NOT NULL DEFAULT '10' COMMENT '折扣，该会员卡享受的折扣优惠,填10就是九折',
  `PRIVILEGE` varchar(500) NOT NULL COMMENT '会员卡特权说明',
  `NOTICE` varchar(16) NOT NULL COMMENT '会员卡提示：结账时出示会员卡',
  `DESCRIPTION` varchar(500) NOT NULL COMMENT '会员卡使用须知说明：会员卡须先领卡再使用',
  `CARD_STATUS` int(1) NOT NULL DEFAULT '0' COMMENT '会员卡状态：-1删除 0正常 1失效',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='商户会员卡';

/*Data for the table `merchant_card` */

insert  into `merchant_card`(`ID`,`MERCHANT_ID`,`MERCHANT_NO`,`MERCHANT_CARD_ID`,`WX_CARD_ID`,`BRAND_LOGO`,`BRAND_NAME`,`CARD_NAME`,`CARD_BACK_URL`,`CARD_BACK_COLOR`,`DISCOUNT`,`PRIVILEGE`,`NOTICE`,`DESCRIPTION`,`CARD_STATUS`,`CREATE_TIME`) values 
(1,1,'000000','2019102617110000',NULL,'http://www.baidu.com','永辉超市','微信会员卡体验','http://www.mi.com','#Color10',1,'会员卡特权说明','会员卡提示：结账时出示会员卡','会员卡使用须知：须会员领卡后才能使用',0,'2019-10-26 17:19:14');

/*Table structure for table `merchant_card_basic` */

DROP TABLE IF EXISTS `merchant_card_basic`;

CREATE TABLE `merchant_card_basic` (
  `ID` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MERCHANT_CARD_ID` varchar(64) NOT NULL COMMENT '商户会员卡ID',
  `MERCHANT_NO` varchar(50) NOT NULL COMMENT '商户编号',
  `OPEN_METHOD` int(1) NOT NULL DEFAULT '1' COMMENT '开卡方式 1直接开卡 2预存开卡  3付费开卡',
  `OPEN_MONEY` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '开卡金额：默认0.00  开放方式是直接开卡，此字段无效',
  `OPEN_ATTR` varchar(50) NOT NULL DEFAULT '1,2' COMMENT '开卡属性 1姓名 2手机号 3兴趣 4生日 5性别 6邮箱',
  `ACTIVATE_TYPE` int(1) DEFAULT NULL COMMENT '激活类型： 1自动激活 AUTO_ACTIVATE 2一键激活 WX_ACTIVATE',
  `ACTIVATE_URL` varchar(500) DEFAULT NULL COMMENT '激活地址',
  `SUPPLY_BONUS` int(1) DEFAULT NULL COMMENT '是否显示积分：true或false',
  `BONUS_URL` varchar(125) DEFAULT NULL COMMENT '设置跳转外链查看积分详情地址',
  `SUPPLY_BALANCE` int(1) DEFAULT NULL COMMENT '是否支持储值：true或false',
  `BALANCE_URL` varchar(125) DEFAULT NULL COMMENT '设置跳转外链查看余额详情地址',
  `DISPLAY_FIELD` varchar(50) DEFAULT '1' COMMENT '栏位展示  1积分 POINT  2余额 BALANCE  3优惠券 COUPON 4等级 LEVEL (只能有三项)',
  `BAR_TYPE` int(1) NOT NULL DEFAULT '1' COMMENT '条码展示：1条形码(CODE_TYPE_BARCODE) 2二维码(CODE_TYPE_QRCODE) 3文本(CODE_TYPE_TEXT)',
  `CENTER_TYPE` int(1) NOT NULL DEFAULT '1' COMMENT '中心按钮类型：1会员支付 2微信支付 3自定义 4不展示',
  `CENTER_TEXT` varchar(50) NOT NULL COMMENT '中心按钮文案：会员支付 微信支付 自定义支付',
  `CENTER_SUB_TEXT` varchar(50) NOT NULL COMMENT '中心按钮提示： 点击生成付款码',
  `VALID_TYPE` int(1) DEFAULT NULL COMMENT '有效期：1永久有效 DATE_TYPE_PERMANENT  2固定范围有效DATE_TYPE_FIX_TIME_RANGE 3自领取日 DATE_TYPE_FIX_TERM (单位为天当天有效输入0)',
  `BEGIN_TIME` int(11) DEFAULT NULL COMMENT '有效日期开始时间  永久有效不填 2018-01-01',
  `END_TIME` int(11) DEFAULT NULL COMMENT '有效日期结束时间  永久有效不填 2018-12-31',
  `LIMIT_TIME` int(11) DEFAULT NULL COMMENT '可用时段 例：周一至周五 全天 限制类型枚举值：支持填入 MONDAY 周一 TUESDAY 周二 WEDNESDAY 周三 THURSDAY 周四 FRIDAY 周五 SATURDAY 周六 SUNDAY 周日',
  `LIMIT_BEGIN` varchar(10) DEFAULT NULL COMMENT '可用时段：开始时间10：00',
  `LIMIT_END` varchar(10) DEFAULT NULL COMMENT '可用时段：结束时间23：59',
  `CARD_LIMIT` int(1) NOT NULL DEFAULT '1' COMMENT '每人可领券的数量限制，建议会员卡每人限领一张',
  `IS_SUPPORT_STORE` int(1) NOT NULL DEFAULT '0' COMMENT '是否支持所有门店：0否 1是',
  `SUPPORT_STORES` varchar(255) DEFAULT NULL COMMENT '适用门店ID',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='商户会员卡拓展表';

/*Data for the table `merchant_card_basic` */

insert  into `merchant_card_basic`(`ID`,`MERCHANT_CARD_ID`,`MERCHANT_NO`,`OPEN_METHOD`,`OPEN_MONEY`,`OPEN_ATTR`,`ACTIVATE_TYPE`,`ACTIVATE_URL`,`SUPPLY_BONUS`,`BONUS_URL`,`SUPPLY_BALANCE`,`BALANCE_URL`,`DISPLAY_FIELD`,`BAR_TYPE`,`CENTER_TYPE`,`CENTER_TEXT`,`CENTER_SUB_TEXT`,`VALID_TYPE`,`BEGIN_TIME`,`END_TIME`,`LIMIT_TIME`,`LIMIT_BEGIN`,`LIMIT_END`,`CARD_LIMIT`,`IS_SUPPORT_STORE`,`SUPPORT_STORES`) values 
(1,'2019102617110000','000000',1,0.00,'1,2',1,'http://www.baidu.com',1,'http://www.mi.com',1,'http://www.qq.com','1,2,3',3,1,'会员支付','点击生成付款码',2,20191027,20191030,2,'8','24',1,0,'1');

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

/*Table structure for table `merchant_card_sku` */

DROP TABLE IF EXISTS `merchant_card_sku`;

CREATE TABLE `merchant_card_sku` (
  `ID` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MERCHANT_ID` bigint(64) NOT NULL COMMENT '商户ID',
  `MERCHANT_NO` varchar(50) NOT NULL COMMENT '商户编号',
  `MERCHANT_CARD_ID` varchar(64) NOT NULL COMMENT '商户会员卡ID',
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

/*Table structure for table `t_seckill_order` */

DROP TABLE IF EXISTS `t_seckill_order`;

CREATE TABLE `t_seckill_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ORDER_NO` varchar(36) NOT NULL DEFAULT '-1' COMMENT '代理商订单号',
  `MOBILE` varchar(36) NOT NULL DEFAULT '-1' COMMENT '用户手机号',
  `PROD_ID` varchar(36) NOT NULL DEFAULT '-1' COMMENT '商品id',
  `PROD_NAME` varchar(36) NOT NULL DEFAULT '-1' COMMENT '商品名称',
  `CHARGE_MONEY` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '交易金额',
  `CHARGE_TIME` datetime DEFAULT NULL COMMENT '订单下单时间',
  `FINISH_TIME` datetime DEFAULT NULL COMMENT '订单结束时间',
  `ORDER_STATUS` tinyint(4) NOT NULL DEFAULT '1' COMMENT '订单状态，1 初始化 2 处理中 3 失败 0 成功',
  `RECORD_STATUS` tinyint(4) NOT NULL DEFAULT '0' COMMENT '记录状态 0 正常 1 已删除',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `t_seckill_order` */

insert  into `t_seckill_order`(`id`,`ORDER_NO`,`MOBILE`,`PROD_ID`,`PROD_NAME`,`CHARGE_MONEY`,`CHARGE_TIME`,`FINISH_TIME`,`ORDER_STATUS`,`RECORD_STATUS`,`CREATE_TIME`,`UPDATE_TIME`) values 
(1,'1e35bb07-87f6-4814-84ab-6717adc5822f','15218725510','pid_0001','iphoneX2019新款',5999.000,'2019-11-15 18:12:35',NULL,2,0,'2019-11-15 18:12:35','2019-11-15 18:12:35');

/*Table structure for table `t_seckill_product` */

DROP TABLE IF EXISTS `t_seckill_product`;

CREATE TABLE `t_seckill_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `PROD_ID` varchar(36) NOT NULL DEFAULT '-1' COMMENT '商品id',
  `PROD_NAME` varchar(36) NOT NULL DEFAULT '-1' COMMENT '商品名称',
  `PROD_STATUS` int(1) NOT NULL DEFAULT '0' COMMENT '商品状态,0-上架，1-下架',
  `PROD_STOCK` int(11) NOT NULL DEFAULT '0' COMMENT '商品库存',
  `PROD_PRICE` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '商品售价',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `VERSION` int(11) NOT NULL DEFAULT '0' COMMENT '更新版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `t_seckill_product` */

insert  into `t_seckill_product`(`id`,`PROD_ID`,`PROD_NAME`,`PROD_STATUS`,`PROD_STOCK`,`PROD_PRICE`,`CREATE_TIME`,`UPDATE_TIME`,`VERSION`) values 
(1,'pid_0001','iphoneX2019新款',0,95,5999.000,'2019-11-15 15:39:21','2019-11-15 15:39:21',0),
(2,'pid_0002','小米9SE',0,300,1200.000,'2019-11-15 15:39:21','2019-11-15 15:39:21',0),
(3,'pid_0003','华为MATE20',0,400,2000.000,'2019-11-15 15:39:21','2019-11-15 15:39:21',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
