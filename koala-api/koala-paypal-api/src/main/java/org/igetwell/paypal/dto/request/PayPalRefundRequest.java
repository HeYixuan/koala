package org.igetwell.paypal.dto.request;

import org.igetwell.common.enums.PayChannel;

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
    private PayChannel channel;

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

    public PayChannel getChannel() {
        return channel;
    }

    public void setChannel(PayChannel channel) {
        this.channel = channel;
    }

    public PayPalRefundRequest() {
    }

    public PayPalRefundRequest(PayChannel channel, String outNo, String tradeNo, String transactionId, BigDecimal totalFee, BigDecimal fee) {
        this.channel = channel;
        this.outNo = outNo;
        this.tradeNo = tradeNo;
        this.transactionId = transactionId;
        this.totalFee = totalFee;
        this.fee = fee;
    }
}
