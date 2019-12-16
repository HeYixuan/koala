package org.igetwell.wechat.sdk.bean.card.code.consume;

import lombok.Getter;
import lombok.Setter;

/**
 * 设置买单
 */
@Getter
@Setter
public class SupplyPay {

    /**
     * 卡券ID
     */
    private String cardId;

    /**
     * 是否开启买单功能，填true/false
     */
    private Boolean isOpen = true;
}
