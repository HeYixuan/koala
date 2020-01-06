package org.igetwell.system.bean.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class RefundRequest implements Serializable {

    /**
     * 商户交易订单号
     */
    private String tradeNo;

    /**
     * 微信支付单号
     */
    private String transactionId;

    /**
     * 退款单号
     */
    private String outNo;

    /**
     * 退款订单总金额,单位为分
     */
    private BigDecimal totalFee;

    /**
     * 退款金额,单位为分
     */
    private BigDecimal fee;



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

    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo;
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

    public RefundRequest() {
    }

    public RefundRequest(String tradeNo, String transactionId, String outNo, BigDecimal totalFee, BigDecimal fee) {
        this.tradeNo = tradeNo;
        this.transactionId = transactionId;
        this.outNo = outNo;
        this.totalFee = totalFee;
        this.fee = fee;
    }
}
