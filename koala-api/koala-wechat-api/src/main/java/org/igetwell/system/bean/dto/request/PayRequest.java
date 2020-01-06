package org.igetwell.system.bean.dto.request;

import org.igetwell.common.enums.TradeType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付请求参数
 */
public class PayRequest implements Serializable {

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
     * 支付类型
     */
    private TradeType tradeType;

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

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public PayRequest() {
    }

    public PayRequest(String tradeNo, String productId, String body, BigDecimal fee, TradeType tradeType) {
        this.tradeNo = tradeNo;
        this.productId = productId;
        this.body = body;
        this.fee = fee;
        this.tradeType = tradeType;
    }
}
