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
     * 货币代码：CNY-人民币 UDS-美元 默认CNY
     */
    private String currency;

    /**
     * 商户ID
     */
    private Long mchId;

    /**
     * 商户号
     */
    private String mchNo;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public RefundOrder() {
    }

    public RefundOrder(Long id, String outNo, String tradeNo, String transactionId, BigDecimal totalFee, BigDecimal refundFee, Long mchId, String mchNo, Long memberId, String memberNo, Integer status) {
        this.id = id;
        this.outNo = outNo;
        this.tradeNo = tradeNo;
        this.transactionId = transactionId;
        this.totalFee = totalFee;
        this.refundFee = refundFee;
        this.mchId = mchId;
        this.mchNo = mchNo;
        this.memberId = memberId;
        this.memberNo = memberNo;
        this.status = status;
    }
}