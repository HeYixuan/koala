package org.igetwell.common.constans.cache;

public interface OrderKey {

    /**
     * 商品库存KEY
     */
    String COMPONENT_STOCK = "COMPONENT_STOCK_%s";

    String STOCK_LOCK = "STOCK_LOCK_%s";

    /**
     * 支付单号KEY
     */
    String TRADE_ORDER = "NO：%s";

    /**
     * 退款单号支付单号KEY
     */
    String REFUND_ORDER = "T：%s";
}
