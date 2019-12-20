package org.igetwell.system.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RefundOrder implements Serializable {
    private Long id;

    /**
     * 商户退款单号
     */
    private String outNo;

    /**
     * 商户支付单号
     */
    private String tradeNo;

    /**
     * 微信支付单号
     */
    private String transactionId;

    /**
     * 微信退款单号
     */
    private String outRefundNo;

    /**
     * 退款入账账户
     */
    private String refundAccount;

    /**
     * 订单总金额，单位为分
     */
    private BigDecimal totalFee;

    /**
     * 应结订单金额：应结订单金额=订单金额-非充值代金券金额，应结订单金额
     */
    private BigDecimal settleTotalFee;

    /**
     * 申请退款金额,单位为分
     */
    private BigDecimal refundFee;

    /**
     * 退款金额：退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     */
    private BigDecimal settleFee;

    /**
     * 商户ID
     */
    private Long merchantId;

    /**
     * 商户号
     */
    private String merchantNo;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员编号
     */
    private String memberNo;

    /**
     * 退款成功时间
     */
    private String refundTime;

    /**
     * 退款状态：0-用户发起退款 1-退款成功 2退款失败 3退款关闭 4商户拒绝退款
     */
    private Integer status;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo;
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

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getRefundAccount() {
        return refundAccount;
    }

    public void setRefundAccount(String refundAccount) {
        this.refundAccount = refundAccount;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getSettleTotalFee() {
        return settleTotalFee;
    }

    public void setSettleTotalFee(BigDecimal settleTotalFee) {
        this.settleTotalFee = settleTotalFee;
    }

    public BigDecimal getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }

    public BigDecimal getSettleFee() {
        return settleFee;
    }

    public void setSettleFee(BigDecimal settleFee) {
        this.settleFee = settleFee;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        RefundOrder other = (RefundOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOutNo() == null ? other.getOutNo() == null : this.getOutNo().equals(other.getOutNo()))
            && (this.getTradeNo() == null ? other.getTradeNo() == null : this.getTradeNo().equals(other.getTradeNo()))
            && (this.getTransactionId() == null ? other.getTransactionId() == null : this.getTransactionId().equals(other.getTransactionId()))
            && (this.getOutRefundNo() == null ? other.getOutRefundNo() == null : this.getOutRefundNo().equals(other.getOutRefundNo()))
            && (this.getRefundAccount() == null ? other.getRefundAccount() == null : this.getRefundAccount().equals(other.getRefundAccount()))
            && (this.getTotalFee() == null ? other.getTotalFee() == null : this.getTotalFee().equals(other.getTotalFee()))
            && (this.getSettleTotalFee() == null ? other.getSettleTotalFee() == null : this.getSettleTotalFee().equals(other.getSettleTotalFee()))
            && (this.getRefundFee() == null ? other.getRefundFee() == null : this.getRefundFee().equals(other.getRefundFee()))
            && (this.getSettleFee() == null ? other.getSettleFee() == null : this.getSettleFee().equals(other.getSettleFee()))
            && (this.getMerchantId() == null ? other.getMerchantId() == null : this.getMerchantId().equals(other.getMerchantId()))
            && (this.getMerchantNo() == null ? other.getMerchantNo() == null : this.getMerchantNo().equals(other.getMerchantNo()))
            && (this.getMemberId() == null ? other.getMemberId() == null : this.getMemberId().equals(other.getMemberId()))
            && (this.getMemberNo() == null ? other.getMemberNo() == null : this.getMemberNo().equals(other.getMemberNo()))
            && (this.getRefundTime() == null ? other.getRefundTime() == null : this.getRefundTime().equals(other.getRefundTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOutNo() == null) ? 0 : getOutNo().hashCode());
        result = prime * result + ((getTradeNo() == null) ? 0 : getTradeNo().hashCode());
        result = prime * result + ((getTransactionId() == null) ? 0 : getTransactionId().hashCode());
        result = prime * result + ((getOutRefundNo() == null) ? 0 : getOutRefundNo().hashCode());
        result = prime * result + ((getRefundAccount() == null) ? 0 : getRefundAccount().hashCode());
        result = prime * result + ((getTotalFee() == null) ? 0 : getTotalFee().hashCode());
        result = prime * result + ((getSettleTotalFee() == null) ? 0 : getSettleTotalFee().hashCode());
        result = prime * result + ((getRefundFee() == null) ? 0 : getRefundFee().hashCode());
        result = prime * result + ((getSettleFee() == null) ? 0 : getSettleFee().hashCode());
        result = prime * result + ((getMerchantId() == null) ? 0 : getMerchantId().hashCode());
        result = prime * result + ((getMerchantNo() == null) ? 0 : getMerchantNo().hashCode());
        result = prime * result + ((getMemberId() == null) ? 0 : getMemberId().hashCode());
        result = prime * result + ((getMemberNo() == null) ? 0 : getMemberNo().hashCode());
        result = prime * result + ((getRefundTime() == null) ? 0 : getRefundTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
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
        sb.append(", outNo=").append(outNo);
        sb.append(", tradeNo=").append(tradeNo);
        sb.append(", transactionId=").append(transactionId);
        sb.append(", outRefundNo=").append(outRefundNo);
        sb.append(", refundAccount=").append(refundAccount);
        sb.append(", totalFee=").append(totalFee);
        sb.append(", settleTotalFee=").append(settleTotalFee);
        sb.append(", refundFee=").append(refundFee);
        sb.append(", settleFee=").append(settleFee);
        sb.append(", merchantId=").append(merchantId);
        sb.append(", merchantNo=").append(merchantNo);
        sb.append(", memberId=").append(memberId);
        sb.append(", memberNo=").append(memberNo);
        sb.append(", refundTime=").append(refundTime);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}