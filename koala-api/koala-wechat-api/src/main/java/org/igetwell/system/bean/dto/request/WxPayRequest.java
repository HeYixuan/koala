package org.igetwell.system.bean.dto.request;

import org.igetwell.common.enums.TradeType;

import java.math.BigDecimal;

/**
 * 微信支付请求参数
 */
public class WxPayRequest extends PayRequest {

    /**
     * 请求端IP
     */
    private String clientIp;

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public WxPayRequest() {
    }

    public WxPayRequest(TradeType tradeType, String tradeNo, String productId, String body, BigDecimal fee, String clientIp) {
        super(tradeNo, productId, body, fee, tradeType);
        this.clientIp = clientIp;
    }
}
