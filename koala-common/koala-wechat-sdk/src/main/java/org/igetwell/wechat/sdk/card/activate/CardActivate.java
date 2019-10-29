package org.igetwell.wechat.sdk.card.activate;

import lombok.Getter;
import lombok.Setter;
/**
 * 卡券激活接口
 */
@Getter
@Setter
public class CardActivate {

    /**
     * 会员卡编号，由开发者填入，作为序列号显示在用户的卡包里。可与Code码保持等值
     */
    private String membershipNumber;

    /**
     * 领取会员卡用户获得的code
     */
    private String code;

    /**
     * 卡券ID,自定义code卡券必填
     */
    private String cardId;

    /**
     * 商家自定义会员卡背景图，须先调用 上传图片接口
     * 将背景图上传至CDN，否则报错，
     * 卡面设计请遵循 微信会员卡自定义背景设计规范
     */
    private String backgroundPicUrl;

    /**
     * 初始积分，不填为0
     */
    private int initBonus;

    /**
     * 积分同步说明。
     */
    private String initBonusRecord;

    /**
     * 初始余额，不填为0。
     */
    private int initBalance;

    /**
     * 创建时字段custom_field1定义类型的初始值，限制为4个汉字，12字节
     */
    private String initCustomFieldValue1;

    /**
     * 创建时字段custom_field2定义类型的初始值，限制为4个汉字，12字节
     */
    private String initCustomFieldValue2;

    /**
     * 创建时字段custom_field3定义类型的初始值，限制为4个汉字，12字节
     */
    private String initCustomFieldValue3;
}
