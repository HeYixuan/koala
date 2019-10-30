package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;

/**
 * 卡券抽象类，公众属性
 */
@Getter
@Setter
public abstract class AbstractCard {

    /**
     * 卡券类型
     */
    private String cardType;
}
