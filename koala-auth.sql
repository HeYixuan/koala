/*
SQLyog Ultimate v12.2.6 (64 bit)
MySQL - 8.0.17 : Database - koala
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`koala` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `koala`;

/*Table structure for table `sys_dept` */

DROP TABLE IF EXISTS `sys_dept`;

CREATE TABLE `sys_dept` (
  `id` bigint(64) NOT NULL COMMENT '主键',
  `tenant_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户ID',
  `parent_id` bigint(64) DEFAULT '0' COMMENT '父主键',
  `ancestors` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '祖级列表',
  `dept_category` int(2) DEFAULT NULL COMMENT '部门类型',
  `name` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门名称',
  `full_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门全称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='机构表';

/*Data for the table `sys_dept` */

insert  into `sys_dept`(`id`,`tenant_id`,`parent_id`,`ancestors`,`dept_category`,`name`,`full_name`,`sort`) values 
(1123598813738675201,'000000',0,'0',1,'刀锋科技','江苏刀锋科技有限公司',1),
(1123598813738675202,'000000',1123598813738675201,'0,1123598813738675201',1,'常州刀锋','常州刀锋科技有限公司',1),
(1123598813738675203,'000000',1123598813738675201,'0,1123598813738675201',1,'苏州刀锋','苏州刀锋科技有限公司',1);

/*Table structure for table `sys_dept_relation` */

DROP TABLE IF EXISTS `sys_dept_relation`;

CREATE TABLE `sys_dept_relation` (
  `parent` bigint(64) NOT NULL COMMENT '祖先节点',
  `child` bigint(64) NOT NULL COMMENT '后代节点',
  PRIMARY KEY (`parent`,`child`),
  KEY `idx1` (`parent`),
  KEY `idx2` (`child`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='部门关系表';

/*Data for the table `sys_dept_relation` */

insert  into `sys_dept_relation`(`parent`,`child`) values 
(1123598813738675201,1123598813738675202),
(1123598813738675201,1123598813738675203);

/*Table structure for table `sys_dict` */

DROP TABLE IF EXISTS `sys_dict`;

CREATE TABLE `sys_dict` (
  `id` bigint(64) NOT NULL COMMENT '主键',
  `parent_id` bigint(64) DEFAULT '0' COMMENT '父主键',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典码',
  `dict_key` int(2) DEFAULT NULL COMMENT '字典值',
  `dict_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典备注',
  `is_deleted` int(2) DEFAULT '0' COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典表';

/*Data for the table `sys_dict` */

insert  into `sys_dict`(`id`,`parent_id`,`code`,`dict_key`,`dict_value`,`sort`,`remark`,`is_deleted`) values 
(1123598814738675201,0,'sex',-1,'性别',1,NULL,0),
(1123598814738675202,1123598814738675201,'sex',1,'男',1,NULL,0),
(1123598814738675203,1123598814738675201,'sex',2,'女',2,NULL,0),
(1123598814738675204,0,'notice',-1,'通知类型',2,NULL,0),
(1123598814738675205,1123598814738675204,'notice',1,'发布通知',1,NULL,0),
(1123598814738675206,1123598814738675204,'notice',2,'批转通知',2,NULL,0),
(1123598814738675207,1123598814738675204,'notice',3,'转发通知',3,NULL,0),
(1123598814738675208,1123598814738675204,'notice',4,'指示通知',4,NULL,0),
(1123598814738675209,1123598814738675204,'notice',5,'任免通知',5,NULL,0),
(1123598814738675210,1123598814738675204,'notice',6,'事务通知',6,NULL,0),
(1123598814738675211,0,'menu_category',-1,'菜单类型',3,NULL,0),
(1123598814738675212,1123598814738675211,'menu_category',1,'菜单',1,NULL,0),
(1123598814738675213,1123598814738675211,'menu_category',2,'按钮',2,NULL,0),
(1123598814738675214,0,'button_func',-1,'按钮功能',4,NULL,0),
(1123598814738675215,1123598814738675214,'button_func',1,'工具栏',1,NULL,0),
(1123598814738675216,1123598814738675214,'button_func',2,'操作栏',2,NULL,0),
(1123598814738675217,1123598814738675214,'button_func',3,'工具操作栏',3,NULL,0),
(1123598814738675218,0,'yes_no',-1,'是否',5,NULL,0),
(1123598814738675219,1123598814738675218,'yes_no',1,'否',1,NULL,0),
(1123598814738675220,1123598814738675218,'yes_no',2,'是',2,NULL,0),
(1123598814738675221,0,'flow',-1,'流程类型',5,NULL,0),
(1123598814738675222,1123598814738675221,'flow',1,'请假流程',1,'leave',0),
(1123598814738675223,1123598814738675221,'flow',2,'报销流程',2,'expense',0),
(1123598814738675224,0,'oss',-1,'对象存储类型',6,NULL,0),
(1123598814738675225,1123598814738675224,'oss',1,'minio',1,NULL,0),
(1123598814738675226,1123598814738675224,'oss',2,'qiniu',2,NULL,0),
(1123598814738675227,0,'org_category',-1,'机构类型',7,NULL,0),
(1123598814738675228,1123598814738675227,'org_category',1,'公司',1,NULL,0),
(1123598814738675229,1123598814738675227,'org_category',2,'部门',2,NULL,0),
(1123598814738675230,1123598814738675227,'org_category',3,'小组',3,NULL,0),
(1123598814738675231,0,'data_scope_type',-1,'数据权限',8,NULL,0),
(1123598814738675232,1123598814738675231,'data_scope_type',1,'全部可见',1,NULL,0),
(1123598814738675233,1123598814738675231,'data_scope_type',2,'本人可见',2,NULL,0),
(1123598814738675234,1123598814738675231,'data_scope_type',3,'所在机构可见',3,NULL,0),
(1123598814738675235,1123598814738675231,'data_scope_type',4,'所在机构及子级可见',4,NULL,0),
(1123598814738675236,1123598814738675231,'data_scope_type',5,'自定义',5,NULL,0),
(1123598814738675237,0,'api_scope_type',-1,'接口权限',10,NULL,0),
(1123598814738675238,1123598814738675237,'api_scope_type',1,'系统接口',1,NULL,0),
(1123598814738675239,1123598814738675237,'api_scope_type',2,'业务接口',2,NULL,0),
(1123598814738675240,0,'scope_category',-1,'权限类型',10,NULL,0),
(1123598814738675241,1123598814738675240,'scope_category',1,'数据权限',1,NULL,0),
(1123598814738675242,1123598814738675240,'scope_category',2,'接口权限',2,NULL,0);

/*Table structure for table `sys_gateway_route` */

DROP TABLE IF EXISTS `sys_gateway_route`;

CREATE TABLE `sys_gateway_route` (
  `id` varchar(64) NOT NULL COMMENT '路由ID',
  `route_name` varchar(64) NOT NULL COMMENT '路由名称',
  `predicates` json DEFAULT NULL COMMENT '断言',
  `filters` json DEFAULT NULL COMMENT '过滤器',
  `uri` varchar(50) DEFAULT NULL,
  `order` int(2) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='服务网关路由配置表';

/*Data for the table `sys_gateway_route` */

insert  into `sys_gateway_route`(`id`,`route_name`,`predicates`,`filters`,`uri`,`order`,`create_time`,`update_time`,`del_flag`) values 
('koala-merchant','商户中心','[{\"args\": {\"_genkey_0\": \"/merchant/**\"}, \"name\": \"Path\"}]','[]','lb://koala-merchant',0,'2019-09-12 06:16:40','2019-09-16 13:25:17','0'),
('koala-oauth','认证中心','[{\"args\": {\"_genkey_0\": \"/oauth/**\"}, \"name\": \"Path\"}]','[{\"args\": {}, \"name\": \"ValidateCodeGatewayFilter\"}, {\"args\": {}, \"name\": \"PasswordDecoderFilter\"}]','lb://koala-oauth',0,'2019-09-12 15:35:18','2019-09-17 16:04:39','0'),
('koala-system','通用权限模块','[{\"args\": {\"_genkey_0\": \"/system/**\"}, \"name\": \"Path\"}]','[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"20\", \"redis-rate-limiter.replenishRate\": \"10\"}, \"name\": \"RequestRateLimiter\"}, {\"args\": {\"name\": \"default\", \"fallbackUri\": \"forward:/fallback\"}, \"name\": \"Hystrix\"}]','lb://koala-system',0,'2019-09-12 15:35:18','2019-09-20 14:53:21','0');

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` bigint(64) NOT NULL COMMENT '主键',
  `parent_id` bigint(64) DEFAULT '0' COMMENT '父级菜单',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单名称',
  `alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单别名',
  `icon_class` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单图标',
  `uri` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求地址',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单资源',
  `menu_type` int(2) DEFAULT NULL COMMENT '菜单类型',
  `sort` int(2) DEFAULT NULL COMMENT '排序',
  `keep_alive` int(2) DEFAULT '1' COMMENT '0开启 1关闭',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单表';

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`id`,`parent_id`,`code`,`name`,`alias`,`icon_class`,`uri`,`source`,`menu_type`,`sort`,`keep_alive`) values 
(1123598815738675201,0,'desk','工作台','menu',NULL,'/desk','iconfont iconicon_airplay',1,1,1),
(1123598815738675202,1123598815738675201,'notice','通知公告','menu',NULL,'/desk/notice','iconfont iconicon_sms',1,1,1),
(1123598815738675203,0,'system','系统管理','menu',NULL,'/system','iconfont iconicon_setting',1,99,1),
(1123598815738675204,1123598815738675203,'user','用户管理','menu',NULL,'/system/user','iconfont iconicon_principal',1,1,1),
(1123598815738675205,1123598815738675203,'dept','机构管理','menu',NULL,'/system/dept','iconfont iconicon_group',1,2,1),
(1123598815738675206,1123598815738675203,'dict','字典管理','menu',NULL,'/system/dict','iconfont iconicon_addresslist',1,3,1),
(1123598815738675207,1123598815738675203,'menu','菜单管理','menu',NULL,'/system/menu','iconfont iconicon_subordinate',1,4,1),
(1123598815738675208,1123598815738675203,'topmenu','顶部菜单','menu',NULL,'/system/topmenu','iconfont icon-canshu',1,5,1),
(1123598815738675209,1123598815738675203,'param','参数管理','menu',NULL,'/system/param','iconfont iconicon_community_line',1,6,1),
(1123598815738675210,0,'monitor','系统监控','menu',NULL,'/monitor','iconfont icon-yanzhengma',1,3,1),
(1123598815738675256,1123598815738675203,'tenant','租户管理','menu',NULL,'/system/tenant','iconfont icon-quanxian',1,7,1),
(1123598815738675307,0,'authority','权限管理','menu',NULL,'/authority','iconfont icon-bofangqi-suoping',1,98,1),
(1123598815738675308,1123598815738675307,'role','角色管理','menu',NULL,'/authority/role','iconfont iconicon_boss',1,1,1),
(1123598815738675309,1123598815738675307,'data_scope','数据权限','menu',NULL,'/authority/datascope','iconfont icon-shujuzhanshi2',1,2,1),
(1123598815738675310,1123598815738675309,'data_scope_setting','数据权限配置','menu',NULL,'/authority/datascope/setting','setting',2,1,1),
(1123598815738675311,1123598815738675307,'api_scope','接口权限','menu',NULL,'/authority/apiscope','iconfont icon-iconset0216',1,3,1),
(1123598815738675312,1123598815738675311,'api_scope_setting','接口权限设置','menu',NULL,'/authority/apiscope/setting','setting',2,1,1);

/*Table structure for table `sys_oauth_client_details` */

DROP TABLE IF EXISTS `sys_oauth_client_details`;

CREATE TABLE `sys_oauth_client_details` (
  `id` bigint(64) NOT NULL COMMENT '主键',
  `client_id` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端id',
  `client_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端密钥',
  `resource_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源集合',
  `scope` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '授权范围',
  `authorized_grant_types` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '授权类型',
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回调地址',
  `authorities` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限',
  `access_token_validity` int(11) NOT NULL COMMENT '令牌过期秒数',
  `refresh_token_validity` int(11) NOT NULL COMMENT '刷新令牌过期秒数',
  `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '附加说明',
  `autoapprove` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '自动授权',
  `tenant_id` int(11) NOT NULL DEFAULT '0' COMMENT '所属租户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='客户端表';

/*Data for the table `sys_oauth_client_details` */

insert  into `sys_oauth_client_details`(`id`,`client_id`,`client_secret`,`resource_ids`,`scope`,`authorized_grant_types`,`web_server_redirect_uri`,`authorities`,`access_token_validity`,`refresh_token_validity`,`additional_information`,`autoapprove`,`tenant_id`) values 
(1123598811738675201,'sword','sword_secret',NULL,'all','refresh_token,password,authorization_code','http://localhost:8888',NULL,3600,604800,NULL,'true',0),
(1123598811738675202,'saber','saber_secret',NULL,'all','refresh_token,password,authorization_code','http://localhost:8080',NULL,3600,604800,NULL,'true',0),
(1123598811738675203,'app','app',NULL,'server','password,refresh_token','http://localhost:8080',NULL,3600,604800,NULL,'true',0),
(1123598811738675204,'daemon','daemon',NULL,'server','password,refresh_token',NULL,NULL,3600,604800,NULL,'true',0),
(1123598811738675205,'gen','gen',NULL,'server','password,refresh_token',NULL,NULL,3600,604800,NULL,'true',0),
(1123598811738675206,'mp','mp',NULL,'server','password,refresh_token',NULL,NULL,3600,604800,NULL,'true',0),
(1123598811738675207,'pig','pig',NULL,'server','password,refresh_token,authorization_code,client_credentials','http://localhost:4040/sso1/login,http://localhost:4041/sso1/login',NULL,3600,604800,NULL,'true',0),
(1123598811738675208,'test','test',NULL,'server','password,refresh_token,authorization_code,client_credentials','https://www.baidu.com/',NULL,3600,604800,NULL,'true',0),
(1123598811738675209,'test2','test2',NULL,'all','password,refresh_token,authorization_code,client_credentials','https://www.baidu.com/',NULL,3600,604800,'{\r\n	\"website\": \"http://www.baidu.com\",\r\n	\"apiKey\": \"7gBZcbsC7kLIWCdELIl8nxcs\",\r\n	\"secretKey\": \"0osTIhce7uPvDKHz6aa67bhCukaKoYl4\",\r\n	\"appName\": \"平台用户认证服务器\",\r\n	\"updateTime\": 1562841065000,\r\n	\"isPersist\": 1,\r\n	\"appOs\": \"\",\r\n	\"appIcon\": \"\",\r\n	\"developerId\": 0,\r\n	\"createTime\": 1542016125000,\r\n	\"appType\": \"server\",\r\n	\"appDesc\": \"资源服务器\",\r\n	\"appId\": \"1552274783265\",\r\n	\"appNameEn\": \"open-cloud-uaa-admin-server\",\r\n	\"status\": 1\r\n}','false',0);

/*Table structure for table `sys_param` */

DROP TABLE IF EXISTS `sys_param`;

CREATE TABLE `sys_param` (
  `id` bigint(64) NOT NULL COMMENT '主键',
  `param_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数名',
  `param_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数键',
  `param_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数值',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='参数表';

/*Data for the table `sys_param` */

insert  into `sys_param`(`id`,`param_name`,`param_key`,`param_value`,`remark`) values 
(1123598819738675201,'是否开启注册功能','account.registerUser','true','开启注册'),
(1123598819738675202,'账号初始密码','account.initPassword','123456','初始密码');

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` bigint(64) NOT NULL COMMENT '主键',
  `tenant_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户ID',
  `parent_id` bigint(64) DEFAULT '0' COMMENT '父主键',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色名',
  `role_alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色别名',
  `ds_type` int(2) DEFAULT '2' COMMENT '数据权限类型：1全部 2本人可见 3所在机构本级可见 4所在机构本级及子级可见 5自定义',
  `ds_scope` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限范围',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

/*Data for the table `sys_role` */

insert  into `sys_role`(`id`,`tenant_id`,`parent_id`,`role_name`,`role_alias`,`ds_type`,`ds_scope`) values 
(1123598816738675201,'000000',0,'超级管理员','ROLE_ADMINISTRATOR',3,NULL),
(1123598816738675202,'000000',0,'用户','ROLE_USER',2,NULL),
(1123598816738675203,'000000',1123598816738675202,'人事','ROLE_HR',2,NULL),
(1123598816738675204,'000000',1123598816738675202,'经理','ROLE_MANAGER',2,NULL),
(1123598816738675205,'000000',1123598816738675202,'老板','ROLE_BOSS',2,NULL);

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_id` bigint(64) DEFAULT NULL COMMENT '菜单id',
  `role_id` bigint(64) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1149888294028800020 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色菜单关联表';

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`id`,`menu_id`,`role_id`) values 
(1149888294028800003,1123598815738675201,1123598816738675201),
(1149888294028800004,1123598815738675202,1123598816738675201),
(1149888294028800005,1123598815738675203,1123598816738675201),
(1149888294028800006,1123598815738675204,1123598816738675201),
(1149888294028800007,1123598815738675205,1123598816738675201),
(1149888294028800008,1123598815738675206,1123598816738675201),
(1149888294028800009,1123598815738675207,1123598816738675201),
(1149888294028800010,1123598815738675208,1123598816738675201),
(1149888294028800011,1123598815738675209,1123598816738675201),
(1149888294028800012,1123598815738675210,1123598816738675201),
(1149888294028800013,1123598815738675256,1123598816738675201),
(1149888294028800014,1123598815738675307,1123598816738675201),
(1149888294028800015,1123598815738675308,1123598816738675201),
(1149888294028800016,1123598815738675309,1123598816738675201),
(1149888294028800017,1123598815738675310,1123598816738675201),
(1149888294028800018,1123598815738675311,1123598816738675201),
(1149888294028800019,1123598815738675312,1123598816738675201);

/*Table structure for table `sys_role_scope` */

DROP TABLE IF EXISTS `sys_role_scope`;

CREATE TABLE `sys_role_scope` (
  `id` bigint(64) NOT NULL COMMENT '主键',
  `scope_category` int(2) DEFAULT NULL COMMENT '权限类型(1:数据权限、2:接口权限)',
  `scope_id` bigint(64) DEFAULT NULL COMMENT '权限id',
  `role_id` bigint(64) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色数据权限关联表';

/*Data for the table `sys_role_scope` */

/*Table structure for table `sys_scope_api` */

DROP TABLE IF EXISTS `sys_scope_api`;

CREATE TABLE `sys_scope_api` (
  `id` bigint(64) NOT NULL COMMENT '主键',
  `menu_id` bigint(64) DEFAULT NULL COMMENT '菜单主键',
  `resource_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源编号',
  `scope_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接口权限名',
  `scope_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接口权限地址',
  `scope_type` int(2) DEFAULT NULL COMMENT '接口权限类型',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接口权限备注',
  `create_user` bigint(64) DEFAULT NULL COMMENT '创建人',
  `create_dept` bigint(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint(64) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(2) DEFAULT NULL COMMENT '状态',
  `is_deleted` int(2) DEFAULT NULL COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='接口权限表';

/*Data for the table `sys_scope_api` */

/*Table structure for table `sys_scope_data` */

DROP TABLE IF EXISTS `sys_scope_data`;

CREATE TABLE `sys_scope_data` (
  `id` bigint(64) NOT NULL COMMENT '主键',
  `menu_id` bigint(64) DEFAULT NULL COMMENT '菜单主键',
  `resource_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源编号',
  `scope_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限名称',
  `scope_field` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限字段',
  `scope_class` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限类名',
  `scope_column` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限字段',
  `scope_type` int(2) DEFAULT NULL COMMENT '数据权限类型',
  `scope_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限值域',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限备注',
  `create_user` bigint(64) DEFAULT NULL COMMENT '创建人',
  `create_dept` bigint(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint(64) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(2) DEFAULT NULL COMMENT '状态',
  `is_deleted` int(2) DEFAULT NULL COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据权限表';

/*Data for the table `sys_scope_data` */

/*Table structure for table `sys_tenant` */

DROP TABLE IF EXISTS `sys_tenant`;

CREATE TABLE `sys_tenant` (
  `id` bigint(64) NOT NULL COMMENT '主键',
  `tenant_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户ID',
  `tenant_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户名称',
  `linkman` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人',
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系电话',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系地址',
  `begin_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `status` char(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '0正常 1-冻结',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` int(2) DEFAULT '0' COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='租户表';

/*Data for the table `sys_tenant` */

insert  into `sys_tenant`(`id`,`tenant_id`,`tenant_name`,`linkman`,`email`,`mobile`,`address`,`begin_time`,`end_time`,`status`,`create_time`,`update_time`,`is_deleted`) values 
(1123598820738675201,'000000','管理组','admin','xxxx@qq.com',' 15218725510','广东省深圳市',NULL,NULL,'0',NULL,NULL,0);

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` bigint(64) NOT NULL COMMENT '主键',
  `tenant_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户ID',
  `username` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '账号',
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `email` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `sex` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '性别：M-男，F-女，N-未知',
  `role_id` bigint(64) DEFAULT NULL COMMENT '角色id',
  `dept_id` bigint(64) DEFAULT NULL COMMENT '部门id',
  `is_enabled` int(1) DEFAULT '1' COMMENT '账户启用状态：0停用 1启用',
  `account_non_expired` int(1) DEFAULT '1' COMMENT '账户是否过期：0过期 1未过期',
  `account_non_locked` int(1) DEFAULT '1' COMMENT '账户是否锁定：0锁定 1未锁定',
  `credentials_non_expired` int(11) DEFAULT '1' COMMENT '凭证是否过期：0过期 1未过期',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_user` bigint(64) DEFAULT NULL COMMENT '创建人',
  `create_dept` bigint(64) DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint(64) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` int(2) DEFAULT '0' COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

/*Data for the table `sys_user` */

insert  into `sys_user`(`id`,`tenant_id`,`username`,`password`,`email`,`mobile`,`birthday`,`sex`,`role_id`,`dept_id`,`is_enabled`,`account_non_expired`,`account_non_locked`,`credentials_non_expired`,`last_login_time`,`create_user`,`create_dept`,`create_time`,`update_user`,`update_time`,`is_deleted`) values 
(1123598821738675201,'000000','admin','$2a$10$L4rfVQP7QL9p3bgE.8vZ3.gB2O2cj6NmymJ413EwfpV8dhEL/6n0S','admin@bladex.vip','123333333333','2018-08-08 00:00:00','M',1123598816738675201,1123598813738675201,1,1,1,1,NULL,1123598821738675201,1123598813738675201,'2018-08-08 00:00:00',1123598821738675201,'2018-08-08 00:00:00',0),
(1123598821738675202,'000000','hr','5e79b90f7bba52d54115f086e48f539016a27ec6','hr@bladex.vip','123333333333','2018-08-23 00:00:00','M',1123598816738675203,1123598813738675203,1,1,1,1,NULL,1123598821738675201,1123598813738675201,'2019-04-27 17:03:10',1123598821738675201,'2019-04-27 17:03:10',0),
(1123598821738675203,'000000','manager','dfbaa3b61caa3a319f463cc165085aa8c822d2ce','manager@bladex.vip','123333333333','2018-08-08 00:00:00','F',1123598816738675204,1123598813738675201,1,1,1,1,NULL,1123598821738675201,1123598813738675201,'2019-04-27 17:03:38',1123598821738675201,'2019-04-27 17:03:38',0),
(1123598821738675204,'000000','boss','abe57d23e18f7ad8ea99c86e430c90a05119a9d3','boss@bladex.vip','123333333333','2018-08-08 00:00:00','F',1123598816738675205,1123598813738675201,1,1,1,1,NULL,1123598821738675201,1123598813738675201,'2019-04-27 17:03:55',1123598821738675201,'2019-04-27 17:03:55',0),
(1123598821738675205,'000000','admin2','$2a$10$L4rfVQP7QL9p3bgE.8vZ3.gB2O2cj6NmymJ413EwfpV8dhEL/6n0S','admin@bladex.vip','123333333333','2018-08-08 00:00:00','N',1123598816738675201,1123598813738675201,1,1,1,1,NULL,1123598821738675201,1123598813738675201,'2018-08-08 00:00:00',1123598821738675201,'2018-08-08 00:00:00',0);

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `user_id` bigint(64) NOT NULL COMMENT '用户ID',
  `role_id` bigint(64) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

/*Data for the table `sys_user_role` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
