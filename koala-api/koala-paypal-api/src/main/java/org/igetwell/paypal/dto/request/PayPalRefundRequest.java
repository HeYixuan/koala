package org.igetwell.paypal.dto.request;

import org.igetwell.common.enums.PayType;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayPalRefundRequest implements Serializable {

    /**
     * 商户退款单号
     */
    private String outNo;
    /**
     * 商户交易单号
     */
    private String tradeNo;
    /**
     * 微信交易单号
     */
    private String transactionId;
    /**
     * 退款订单总额,单位为分
     */
    private BigDecimal totalFee;

    /**
     * 退款金额,单位为分
     */
    private BigDecimal fee;



    /**
     * 支付平台 微信 支付宝
     */
    private PayType payType;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo;
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

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public PayPalRefundRequest() {
    }

    public PayPalRefundRequest(PayType payType, String outNo, String tradeNo, String transactionId, BigDecimal totalFee, BigDecimal fee) {
        this.payType = payType;
        this.outNo = outNo;
        this.tradeNo = tradeNo;
        this.transactionId = transactionId;
        this.totalFee = totalFee;
        this.fee = fee;
    }
}
