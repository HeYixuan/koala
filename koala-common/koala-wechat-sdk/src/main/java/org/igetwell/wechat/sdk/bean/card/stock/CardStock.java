package org.igetwell.wechat.sdk.bean.card.stock;

import lombok.Getter;
import lombok.Setter;

/**
 * 管理卡券-修改库存接口-请求参数
 */
@Getter
@Setter
public class CardStock {

    /**
     * 卡券ID
     */
    private String cardId;

    /**
     * 增加多少库存，支持不填或填0
     */
    private Integer increaseStockValue;
}
