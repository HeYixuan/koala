package org.igetwell.paypal.dto.request;

import org.igetwell.common.enums.PayType;
import org.igetwell.common.enums.TradeType;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayPalRequest implements Serializable {


    /**
     * 商户订单交易号
     */
    private String tradeNo;

    /**
     * 在JSAPI支付的时候传入openId
     */
    private String productId;

    /**
     * 商品信息
     */
    private String body;
    /**
     * 支付金额,单位为分
     */
    private BigDecimal fee;

    /**
     * 请求端IP
     */
    private String clientIp;

    /**
     * 支付类型
     */
    private TradeType tradeType;

    /**
     * 支付平台 微信 支付宝
     */
    private PayType payType;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public PayPalRequest() {
    }

    public PayPalRequest(PayType payType, TradeType tradeType, String tradeNo, String productId, String body, BigDecimal fee, String clientIp) {
        this.payType = payType;
        this.tradeType = tradeType;
        this.tradeNo = tradeNo;
        this.productId = productId;
        this.body = body;
        this.fee = fee;
        this.clientIp = clientIp;
    }

    public PayPalRequest(PayType payType, TradeType tradeType, String tradeNo, String productId, String body, BigDecimal fee) {
        this.payType = payType;
        this.tradeType = tradeType;
        this.tradeNo = tradeNo;
        this.productId = productId;
        this.body = body;
        this.fee = fee;
    }
}
