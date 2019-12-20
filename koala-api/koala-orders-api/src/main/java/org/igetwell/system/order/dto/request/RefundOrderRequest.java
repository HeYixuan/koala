package org.igetwell.system.order.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class RefundOrderRequest implements Serializable {

    /**
     * 商户支付单号
     */
    private String tradeNo;

    /**
     * 微信支付单号
     */
    private String transactionId;

    /**
     * 订单总金额，单位为分
     */
    private BigDecimal totalFee;

    /**
     * 申请退款金额,单位为分
     */
    private BigDecimal refundFee;

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

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
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
}
