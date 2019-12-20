package org.igetwell.system.order.protocol;

/**
 * 申请微信退款消息协议
 */
public class RefundPayProtocol {

    /**
     * 微信支付单号
     */
    private String transactionId;
    /**
     * 商户订单号
     */
    private String tradeNo;

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

    public RefundPayProtocol() {
    }

    public RefundPayProtocol(String transactionId, String tradeNo) {
        this.transactionId = transactionId;
        this.tradeNo = tradeNo;
    }
}
