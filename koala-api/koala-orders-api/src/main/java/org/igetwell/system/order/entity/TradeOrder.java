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
     * 渠道ID
     */
    private Long channelId;

    /**
     * 支付金额
     */
    private BigDecimal fee;

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

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TradeOrder other = (TradeOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTradeNo() == null ? other.getTradeNo() == null : this.getTradeNo().equals(other.getTradeNo()))
            && (this.getTransactionId() == null ? other.getTransactionId() == null : this.getTransactionId().equals(other.getTransactionId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getMchNo() == null ? other.getMchNo() == null : this.getMchNo().equals(other.getMchNo()))
            && (this.getChannelId() == null ? other.getChannelId() == null : this.getChannelId().equals(other.getChannelId()))
            && (this.getFee() == null ? other.getFee() == null : this.getFee().equals(other.getFee()))
            && (this.getClientIp() == null ? other.getClientIp() == null : this.getClientIp().equals(other.getClientIp()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getSuccessTime() == null ? other.getSuccessTime() == null : this.getSuccessTime().equals(other.getSuccessTime()))
            && (this.getBody() == null ? other.getBody() == null : this.getBody().equals(other.getBody()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTradeNo() == null) ? 0 : getTradeNo().hashCode());
        result = prime * result + ((getTransactionId() == null) ? 0 : getTransactionId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getMchNo() == null) ? 0 : getMchNo().hashCode());
        result = prime * result + ((getChannelId() == null) ? 0 : getChannelId().hashCode());
        result = prime * result + ((getFee() == null) ? 0 : getFee().hashCode());
        result = prime * result + ((getClientIp() == null) ? 0 : getClientIp().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getSuccessTime() == null) ? 0 : getSuccessTime().hashCode());
        result = prime * result + ((getBody() == null) ? 0 : getBody().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", tradeNo=").append(tradeNo);
        sb.append(", transactionId=").append(transactionId);
        sb.append(", mchId=").append(mchId);
        sb.append(", mchNo=").append(mchNo);
        sb.append(", channelId=").append(channelId);
        sb.append(", fee=").append(fee);
        sb.append(", clientIp=").append(clientIp);
        sb.append(", status=").append(status);
        sb.append(", successTime=").append(successTime);
        sb.append(", body=").append(body);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}