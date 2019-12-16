package org.igetwell.wechat.sdk.bean.card.code.consume;

import lombok.Getter;
import lombok.Setter;

/**
 * 设置快速买单－设置微信买单接口－请求参数
 */
@Getter
@Setter
public class SelfConsume {

    /**
     * 卡券ID
     */
    private String cardId;

    /**
     * 是否开启自助核销功能，填true/false，默认为false
     */
    private Boolean isOpen = true;
}
