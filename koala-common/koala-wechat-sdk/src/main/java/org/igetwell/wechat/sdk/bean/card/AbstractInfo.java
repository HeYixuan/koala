package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;

/**
 * 卡券信息抽象类，具体卡券的公共信息对象
 */
@Getter
@Setter
public abstract class AbstractInfo {

    /**
     * 卡券基础信息
     */
    private WxCardBasis baseInfo;

    /**
     * 卡券高级信息
     */
    private WxCardHigh advancedInfo;
}
