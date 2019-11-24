package org.igetwell.system.order.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 下单sdk接口返回参数
 */
public class ChargeOrderResponse implements Serializable {

    /**秒杀订单号*/
    private String orderNo;
    /**会员ID*/
    private String memberId;
    /**用户下单手机号*/
    private Integer mobile;
    /**商品ID*/
    private Long goodsId;
    /**用户交易金额*/
    private BigDecimal money;

    public ChargeOrderResponse() {
    }

    public ChargeOrderResponse(String orderNo, String memberId, Integer mobile, Long goodsId, BigDecimal money) {
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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
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
