package org.igetwell.wechat.sdk.bean.card.code.consume;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetPayOrder {

    /**
     * 卡券ID
     */
    private String cardId;

    /**
     * 是否开启买单功能，填true/false
     */
    private Boolean isOpen;
}
