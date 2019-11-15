package org.igetwell.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SeckillOrder implements Serializable {
    private Integer id;

    /**
     * 代理商订单号
     */
    private String orderNo;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 商品id
     */
    private String prodId;

    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 交易金额
     */
    private BigDecimal chargeMoney;

    /**
     * 订单下单时间
     */
    private Date chargeTime;

    /**
     * 订单结束时间
     */
    private Date finishTime;

    /**
     * 订单状态，1 初始化 2 处理中 3 失败 0 成功
     */
    private Integer orderStatus;

    /**
     * 记录状态 0 正常 1 已删除
     */
    private Integer recordStatus;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public Date getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Date chargeTime) {
        this.chargeTime = chargeTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}