package org.igetwell.system.order.dto.request;

import java.io.Serializable;

public class RefundTransactionRequest implements Serializable {

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


    public RefundTransactionRequest() {
    }

    public RefundTransactionRequest(String transactionId, String tradeNo, String outRefundNo, String outNo) {
        this.transactionId = transactionId;
        this.tradeNo = tradeNo;
        this.outRefundNo = outRefundNo;
        this.outNo = outNo;
    }
}
