package org.igetwell.system.order.protocol;

import java.math.BigDecimal;

/**
 * 支付成功回调协议
 */
public class OrderPayProtocol {

    /**
     * 商户交易订单号
     */
    private String tradeNo;

    /**
     * 微信支付宝交易单号
     */
    private String transactionId;

    /**
     * 支付金额
     */
    private BigDecimal totalFee;

    /**
     * 订单支付成功时间
     */
    private String timestamp;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public OrderPayProtocol() {
    }

    public OrderPayProtocol(String tradeNo, String transactionId, BigDecimal totalFee, String timestamp) {
        this.tradeNo = tradeNo;
        this.transactionId = transactionId;
        this.totalFee = totalFee;
        this.timestamp = timestamp;
    }
}
