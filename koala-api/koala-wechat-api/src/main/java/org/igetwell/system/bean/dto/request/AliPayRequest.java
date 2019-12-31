package org.igetwell.system.bean.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class AliPayRequest implements Serializable {

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

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
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
