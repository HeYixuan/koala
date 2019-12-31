package org.igetwell.system.order.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 下单支付SDK请求参数
 */
public class OrderPayRequest implements Serializable {

    /**
     * 秒杀订单号
     */
    private String orderNo;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 用户下单手机号
     */
    private Long mobile;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 用户交易金额
     */
    private BigDecimal money;

    public OrderPayRequest() {
    }

    public OrderPayRequest(String orderNo, Long memberId, Long mobile, Long goodsId, BigDecimal money) {
        this.orderNo = orderNo;
        this.memberId = memberId;
        this.mobile = mobile;
        this.goodsId = goodsId;
        this.money = money;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
