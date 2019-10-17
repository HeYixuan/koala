package org.igetwell.wechat.sdk.card.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 卡券库存
 */
@Getter
@Setter
public class WxCardSku {

    /**
     * 卡券库存的数量，上限为100000000
     * 添加必填
     */
    private Integer quantity;

}
