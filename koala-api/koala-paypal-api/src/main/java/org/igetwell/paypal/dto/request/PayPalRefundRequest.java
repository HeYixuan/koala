package org.igetwell.paypal.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayPalRefundRequest implements Serializable {

    /**
     * 商户交易单号
     */
    private String tradeNo;
    /**
     * 商户退款单号
     */
    private String outNo;
    /**
     * 微信交易单号
     */
    private String transactionId;
    /**
     * 退款金额,单位为分
     */
    private BigDecimal totalFee;

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

    public PayPalRefundRequest() {
    }

    public PayPalRefundRequest(String tradeNo, String outNo, String transactionId, BigDecimal totalFee) {
        this.tradeNo = tradeNo;
        this.outNo = outNo;
        this.transactionId = transactionId;
        this.totalFee = totalFee;
    }
}
