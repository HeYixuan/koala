package org.igetwell.merchant.card;


import lombok.Getter;
import lombok.Setter;

/**
 * 微信会员卡卡券
 */
@Getter
@Setter
public class WxCard {
    /**
     * 卡券类型
     */
    private String cardType;

    /**
     * 微信会员卡
     */
    private WxMemberCard memberCard;

}
