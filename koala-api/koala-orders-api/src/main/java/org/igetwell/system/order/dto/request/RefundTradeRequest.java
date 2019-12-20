package org.igetwell.system.order.dto.request;

import java.io.Serializable;

public class RefundTradeRequest implements Serializable {

    /**
     * 商户支付单号
     */
    private String tradeNo;

    /**
     * 微信支付单号
     */
    private String transactionId;

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

    public RefundTradeRequest() {
    }

    public RefundTradeRequest(String transactionId, String tradeNo) {
        this.transactionId = transactionId;
        this.tradeNo = tradeNo;
    }
}
