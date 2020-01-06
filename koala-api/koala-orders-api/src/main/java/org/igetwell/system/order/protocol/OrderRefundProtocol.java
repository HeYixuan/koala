package org.igetwell.system.order.protocol;

public class OrderRefundProtocol {
    /**
     * 商户退款单号
     */
    private String outNo;
    /**
     * 微信支付单号
     */
    private String transactionId;
    /**
     * 商户订单号
     */
    private String tradeNo;

    /**
     * 微信退款单号
     */
    private String refundId;

    /**
     * 退款入账账户
     */
    private String refundAccount;

    /**
     * 订单支付成功时间
     */
    private String timestamp;

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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundAccount() {
        return refundAccount;
    }

    public void setRefundAccount(String refundAccount) {
        this.refundAccount = refundAccount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public OrderRefundProtocol() {
    }

    public OrderRefundProtocol(String outNo, String transactionId, String tradeNo, String refundId, String refundAccount, String timestamp) {
        this.outNo = outNo;
        this.transactionId = transactionId;
        this.tradeNo = tradeNo;
        this.refundId = refundId;
        this.refundAccount = refundAccount;
        this.timestamp = timestamp;
    }

    public OrderRefundProtocol(String outNo, String transactionId, String tradeNo, String refundAccount, String timestamp) {
        this.outNo = outNo;
        this.transactionId = transactionId;
        this.tradeNo = tradeNo;
        this.refundAccount = refundAccount;
        this.timestamp = timestamp;
    }
}
