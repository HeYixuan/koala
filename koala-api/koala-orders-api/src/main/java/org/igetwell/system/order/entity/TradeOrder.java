package org.igetwell.system.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TradeOrder implements Serializable {
    /**
     * 支付单ID
     */
    private Long id;

    /**
     * 商户支付单号
     */
    private String tradeNo;

    /**
     * 微信支付单号
     */
    private String transactionId;

    /**
     * 商户ID
     */
    private Long mchId;

    /**
     * 商户号
     */
    private String mchNo;

    /**
     * 订单类型：1-充值 2-消费 3-开卡
     */
    private Integer orderType;

    /**
     * 渠道ID
     */
    private Long channelId;

    /**
     * 渠道名称 非库字段(级联查询得到的结果)
     */
    private String channelName;

    /**
     * 支付金额
     */
    private BigDecimal fee;

    /**
     * 货币代码：CNY-人民币 UDS-美元 默认CNY
     */
    private String currency;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 支付状态：0-订单生成 1-支付中 2-支付成功 3-支付失败 4-用户取消支付 5-已退款订单
     */
    private Integer status;

    /**
     * 支付成功时间
     */
    private String successTime;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品描述信息
     */
    private String body;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getMchNo() {
        return mchNo;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public TradeOrder() {
    }

    public TradeOrder(Long id, String tradeNo, Long mchId, String mchNo, Integer orderType, Long channelId, BigDecimal fee, String clientIp, Integer status, Long goodsId, String body) {
        this.id = id;
        this.tradeNo = tradeNo;
        this.mchId = mchId;
        this.mchNo = mchNo;
        this.orderType = orderType;
        this.channelId = channelId;
        this.fee = fee;
        this.clientIp = clientIp;
        this.status = status;
        this.goodsId = goodsId;
        this.body = body;
    }
}