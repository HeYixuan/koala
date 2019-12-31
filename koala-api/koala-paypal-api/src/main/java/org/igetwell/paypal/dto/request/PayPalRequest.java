package org.igetwell.paypal.dto.request;

import org.igetwell.common.enums.TradeType;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayPalRequest implements Serializable {

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品信息
     */
    private String body;
    /**
     * 支付金额,单位为分
     */
    private BigDecimal fee;

    /**
     * 用户标识
     */
    private String openId;
    /**
     * 支付类型
     */
    private TradeType tradeType;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }


    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
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
}
