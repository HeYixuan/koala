-- auto-generated definition
create table MERCHANT
(
    ID                 bigint(64)  not null auto_increment,
    MERCHANT_NO        varchar(50) not null comment '商户编号',
    MERCHANT_NAME      varchar(50) null comment '商户名称',
    MERCHANT_EMAIL     varchar(12) null comment '商户邮箱',
    MERCHANT_MOBILE    varchar(13) not null comment '商户手机号',
    MERCHANT_PARENT_ID bigint(64)  null comment '上级商户',
    constraint MERCHANT_ID_UINDEX
        unique (ID),
    constraint MERCHANT_MERCHANT_NO_UINDEX
        unique (MERCHANT_NO)
)
    comment '商户表';

alter table MERCHANT
    add primary key (ID);



-- auto-generated definition
create table MERCHANT_CARD
(
    ID              bigint(64)                          not null auto_increment comment '商户卡ID'
        primary key,
    MERCHANT_ID     bigint(64)                          not null comment '商户ID',
    MERCHANT_NO     varchar(50)                         not null comment '商户编号',
    CARD_NAME       varchar(50)                         not null comment '会员卡名称',
    CARD_SUB_NAME   varchar(50)                         not null comment '会员卡副名称',
    CARD_LOGO       varchar(125)                        not null comment '会员卡LOGO',
    CARD_BACK_URL   varchar(125)                        null comment '会员卡背景图',
    CARD_BACK_COLOR varchar(50)                         not null comment '会员卡颜色',
    CARD_STATUS     int(1)    default 0                 not null comment '会员卡状态：-1删除 0正常 1失效',
    CREATE_TIME     timestamp default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '商户会员卡';

-- auto-generated definition
create table MERCHANT_CARD_BONUS_RULE
(
    ID                  bigint(64)  not null auto_increment comment '主键'
        primary key,
    MERCHANT_CARD_ID    bigint(64)  not null comment '商户会员卡ID',
    MERCHANT_NO         varchar(50) not null comment '商户编号',
    COST_MONEY          bigint(64)  not null comment '消费金额 以分为单位',
    COST_INCR_BONUS     bigint(64)  null comment '对应消费增加的积分',
    MAX_BONUS           bigint(64)  not null comment '用户单次可获取的积分上限',
    COST_BONUS          bigint(64)  null comment '每使用5积分',
    REDUCE_MONEY        bigint(64)  not null comment '抵扣xx元，（这里以分为单位）',
    REDUCE_BONUS_FACTOR bigint(64)  not null comment '抵扣条件，满xx元（这里以分为单位）可用',
    MAX_REDUCE_BONUS    bigint(64)  not null comment '抵扣条件，单笔最多使用xx积分'
)
    comment '商户会员卡积分规则';

-- auto-generated definition
create table MERCHANT_CARD_CUSTOMIZE
(
    ID               bigint(64)   not null auto_increment comment '主键'
        primary key,
    MERCHANT_CARD_ID bigint(64)   not null comment '商户会员卡ID',
    MERCHANT_NO      varchar(50)  not null comment '商户编号',
    MAIN_NAME        varchar(50)  not null comment '入口名称',
    MAIN_URL         varchar(200) null comment '入口URL',
    MAIN_NOTICE      varchar(50)  null comment '入口提示语',
    MAIN_TYPE        int(1)       null comment '入口类型：1必填入口 2自定义 3营销入口'
)
    comment '商户会员卡自定义入口';

-- auto-generated definition
create table MERCHANT_CARD_EXPAND
(
    ID                   bigint(64) auto_increment    not null comment '主键'
        primary key,
    MERCHANT_CARD_ID     bigint(64)                   not null comment '商户会员卡ID',
    MERCHANT_NO          varchar(50)                  not null comment '商户编号',
    OPEN_METHOD          int(1)            default 1     not null comment '开卡方式 1直接开卡 2预存开卡  3付费开卡',
    OPEN_MONEY           decimal(11, 2) default 0.00  not null comment '开卡金额：默认0.00  开放方式是直接开卡，此字段无效',
    OPEN_ATTR            varchar(50)    default '1,2' not null comment '开卡属性 1姓名 2手机号 3兴趣 4生日 5性别 6邮箱',
    CARD_NOTICE          varchar(50)                  not null comment '会员卡提示：结账时出示会员卡',
    CARD_DESCRIPTION     varchar(50)                  not null comment '会员卡使用说明：会员卡须先领卡再使用',
    CARD_PRIVILEGE       varchar(50)                  not null comment '会员卡特权说明',
    CARD_VALID_TYPE      int(1)                       null comment '有效期：1永久有效 DATE_TYPE_PERMANENT  2固定范围有效DATE_TYPE_FIX_TIME_RANGE 3自领取日 DATE_TYPE_FIX_TERM (单位为天当天有效输入0)',
    CARD_BEGIN           int                          null comment '有效日期开始时间  永久有效不填 2018-01-01',
    CARD_END             int                          null comment '有效日期结束时间  永久有效不填 2018-12-31',
    CARD_LIMIT_TIME      int                          null comment '可用时段 例：周一至周五 全天 限制类型枚举值：支持填入 MONDAY 周一 TUESDAY 周二 WEDNESDAY 周三 THURSDAY 周四 FRIDAY 周五 SATURDAY 周六 SUNDAY 周日',
    CARD_LIMIT_BEGIN     varchar(10)                  null comment '可用时段：开始时间10：00',
    CARD_LIMIT_END       varchar(10)                  null comment '可用时段：结束时间23：59',
    ACTIVATE_TYPE        int(1)                       null comment '激活类型： 1自动激活 AUTO_ACTIVATE 2一键激活 WX_ACTIVATE',
    DISPLAY_POINT        int(1)                       null comment '是否显示积分：true或false',
    CARD_POINT_URL       varchar(125)                 null comment '设置跳转外链查看积分详情地址',
    CARD_SUPPORT_BALANCE int(1)                       null comment '是否支持储值：true或false',
    CARD_BALANCE_URL     varchar(125)                 null comment '设置跳转外链查看余额详情地址',
    CARD_DISPLAY_FIELD   varchar(50)    default '1'   null comment '栏位展示  1积分 POINT  2余额 BALANCE  3优惠券 COUPON 4等级 LEVEL (只能有三项)',
    CARD_DISPLAY_BAR     int(1)         default 1     not null comment '条码展示：1条形码(CODE_TYPE_BARCODE) 2二维码(CODE_TYPE_QRCODE) 3文本(CODE_TYPE_TEXT)',
    CARD_CENTER_TYPE     int(1)         default 1     not null comment '中心按钮类型：1会员支付 2微信支付 3自定义 4不展示',
    CARD_CENTER_BUTTON   varchar(50)                  not null comment '中心按钮文案：会员支付 微信支付 自定义支付',
    CARD_CENTER_TIPS     varchar(50)                  not null comment '中心按钮提示： 点击生成付款码',
    CARD_LIMIT           int(1)         default 1     not null comment '每人可领券的数量限制，建议会员卡每人限领一张',
    SUPPORT_STORES       int(1)         default 0     not null comment '是否支持所有门店：0否 1是'
)
    comment '商户会员卡拓展表';

-- auto-generated definition
create table MERCHANT_CARD_SKU
(
    ID               bigint(64) auto_increment not null comment '主键'
        primary key,
    MERCHANT_ID      bigint(64)       not null comment '商户ID',
    MERCHANT_NO      varchar(50)      not null comment '商户编号',
    MERCHANT_CARD_ID bigint(64)       not null comment '商户会员卡ID',
    TOTAL_AMOUNT     int(9) default 1 not null comment '库存总数量：默认为1,最大为100000000',
    SURPLUS_AMOUNT   int(9) default 1 not null comment '剩余数量：默认为1,最大为100000000',
    USE_AMOUNT       int(9) default 0 not null comment '已领取库存数量'
)
    comment '商户会员卡库存表';

-- auto-generated definition
create table MERCHANT_STORE
(
    ID                         bigint(64) auto_increment not null comment '主键'
        primary key,
    MERCHANT_ID                bigint(64)       not null comment '商户ID',
    MERCHANT_NO                varchar(50)      not null comment '商户编号',
    MERCHANT_SID               bigint(64)       not null comment '商户门店ID',
    MERCHANT_POI_ID            bigint(64)       not null comment '创建门店后微信返回的POI_ID 用作查询门店是否审核通过',
    MERCHANT_STORE_NAME        varchar(50)      not null comment '门店名称',
    MERCHANT_SUB_STORE_NAME    varchar(50)      not null comment '分店名称',
    PROVINCE                   varchar(20)      not null comment '省',
    CITY                       varchar(20)      not null comment '市',
    DISTRICT                   varchar(20)      not null comment '区',
    STREET                     varchar(50)      not null comment '详细地址：街道信息',
    MERCHANT_STORE_MOBILE      varchar(13)      not null comment '门店电话',
    MERCHANT_STORE_CLAZZ       varchar(100)     not null comment '门店类型(数组)：不同级分类用“,”隔开',
    MERCHANT_STORE_LOCATION    varchar(50)      not null comment '门店坐标',
    OFFSET_TYPE                int(1) default 1 not null comment '坐标类型： 1 为火星坐标 2 为sogou经纬度 3 为百度经纬度 4 为mapbar经纬度 5 为GPS坐标 6 为sogou墨卡托坐标 注：高德经纬度无需转换可直接使用',
    MERCHANT_STORE_IMAGE       varchar(500)     null comment '门店图片列表（json格式）',
    MERCHANT_STORE_OPEN_TIME   varchar(20)      null comment '营业时间，24 小时制表示，用“-”连接，如 8:00-20:00',
    MERCHANT_STORE_RECOMMEND   varchar(200)     null comment '推荐品',
    MERCHANT_STORE_SPECIAL     varchar(200)     null comment '特色服务',
    MERCHANT_STORE_DESCRIPTION varchar(300)     null comment '商户简介',
    MERCHANT_STORE_PRICE       int    default 1 null comment '人均价格',
    MERCHANT_STORE_PARENT_SID  bigint(64)       null comment '上级门店SID',
    constraint MERCHANT_STORE_MERCHANT_POI_ID_UINDEX
        unique (MERCHANT_POI_ID),
    constraint MERCHANT_STORE_MERCHANT_SID_UINDEX
        unique (MERCHANT_SID)
)
    comment '商户门店表';

