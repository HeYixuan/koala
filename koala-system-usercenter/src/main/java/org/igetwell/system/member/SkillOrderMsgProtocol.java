package org.igetwell.system.member;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @desc 订单结果通知协议
 */

public class SkillOrderMsgProtocol implements Serializable {

    private static final long serialVersionUID = 73717163386598209L;

    /**订单号*/
    private String orderNo;
    /**用户下单手机号*/
    private String mobile;
    /**商品id*/
    private String prodId;
    /**用户交易金额*/
    private BigDecimal money;

    public SkillOrderMsgProtocol(){

    }

    public SkillOrderMsgProtocol(String orderNo, String mobile, String prodId, BigDecimal money){
        this.orderNo = orderNo;
        this.mobile = mobile;
        this.prodId = prodId;
        this.money = money;
    }


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
