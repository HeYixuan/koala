package org.igetwell.system.order.protocol;

public class RefundPayCallProtocol {

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
     * 退款成功时间
     */
    private String refundTime;

    /**
     * 退款状态：SUCCESS-退款成功CHANGE-退款异常REFUNDCLOSE—退款关闭
     */
    private String status;

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

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RefundPayCallProtocol() {
    }

    public RefundPayCallProtocol(String transactionId, String tradeNo, String outRefundNo, String outNo, String refundAccount, String refundTime, String status) {
        this.transactionId = transactionId;
        this.tradeNo = tradeNo;
        this.outRefundNo = outRefundNo;
        this.outNo = outNo;
        this.refundAccount = refundAccount;
        this.refundTime = refundTime;
        this.status = status;
    }
}
