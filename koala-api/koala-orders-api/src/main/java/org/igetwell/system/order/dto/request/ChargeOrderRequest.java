package org.igetwell.system.order.dto.request;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 下单SDK接口请求参数
 */
public class ChargeOrderRequest implements Serializable {

    /**
     * 会员ID
     */
    private Long memberId;
    /**手机号*/
    private Long mobile;
    /**金额*/
    private BigDecimal money;
    /**商品ID*/
    private Long goodsId;

    public ChargeOrderRequest() {
    }

    public ChargeOrderRequest(Long memberId, Long mobile, BigDecimal money, Long goodsId) {
        this.memberId = memberId;
        this.mobile = mobile;
        this.money = money;
        this.goodsId = goodsId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
