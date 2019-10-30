package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;

/**
 * 会员卡
 */
@Getter
@Setter
public class AbstractMemberCard extends Discount {

    /**
     * 商家自定义会员卡背景图，须 先调用 上传图片接口 将背景图上传至CDN，否则报错，
     * 卡面设计请遵循 微信会员卡自定义背景设计规范 ,像素大小控制在 1000像素*600像素以下
     */
    private String backgroundPicUrl;

    /**
     * 会员卡特权说明,限制1024汉字。
     * 添加必填
     */
    private String prerogative;

    /**
     * 设置为true时用户领取会员卡后系统自动将其激活，
     * 无需调用激活接口，详情见 自动激活
     */
    private boolean autoActivate;

    /**
     * 设置为true时会员卡支持一键开卡，
     * 不允许同时传入activate_url字段，
     * 否则设置wx_activate失效。
     * 填入该字段后仍需调用接口设置开卡项方可生效，
     * 详情见 一键开卡 。
     */
    private boolean wxActivate;

    /**
     * 显示积分，填写true或false，如填写true，
     * 积分相关字段均为必 填 若设置为true则后续不可以被关闭。
     */
    private boolean supplyBonus;

    /**
     * 设置跳转外链查看积分详情
     */
    private String bonusUrl;
    /**
     * 是否支持储值，填写true或false。
     * 如填写true，储值相关字段均为必填
     * 若设置为true则后续不可以被关闭。
     * 该字段须开通储值功能后方可使用，
     * 详情见： 获取特殊权限
     */
    private boolean supplyBalance;

    /**
     * 设置跳转外链查看余额详情
     */
    private String balanceUrl;

    /**
     * 定义会员信息类目
     */
    private WxCardClazzField customField1;
    /**
     * 定义会员信息类目
     */
    private WxCardClazzField customField2;

    /**
     * 定义会员信息类目
     */
    private WxCardClazzField customField3;

    /**
     * 积分清零规则说明
     */
    private String bonusCleared;

    /**
     * 积分规则说明
     */
    private String bonusRules;

    /**
     * 储值说明
     */
    private String balanceRules;

    /**
     * 激活会员卡的url
     */
    private String activateUrl;

    /**
     * 激活会原卡url对应的小程序user_name，仅可跳转该公众号绑定的小程序
     */
    private String activateAppBrandUserName;

    /**
     * 激活会原卡url对应的小程序path
     */
    private String activateAppBrandPass;

    /**
     * 自定义会员信息类目
     */
    private WxCardCustomerCell customCell1;

    /**
     * 积分规则
     */
    private WxCardBoundRule bonusRule;

    /**
     * 折扣，该会员卡享受的折扣优惠,填10就是九折。
     */
    //private Integer discount;

}
