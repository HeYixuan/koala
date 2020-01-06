package org.igetwell.system.bean.dto.request;

import org.igetwell.common.enums.TradeType;

import java.math.BigDecimal;

/**
 * 支付宝支付请求参数
 */
public class AliPayRequest extends PayRequest {

    public AliPayRequest() {
        super();
    }

    public AliPayRequest(TradeType tradeType, String tradeNo, String productId, String body, BigDecimal fee) {
        super(tradeNo, productId, body, fee, tradeType);
    }
}
