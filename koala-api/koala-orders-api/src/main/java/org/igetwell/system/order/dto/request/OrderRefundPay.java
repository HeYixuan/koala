package org.igetwell.system.order.dto.request;

import java.math.BigDecimal;

public class OrderRefundPay {

    /**
     * 商户交易订单号
     */
    private String tradeNo;


    /**
     * 申请退款金额,单位为分
     */
    private BigDecimal refundFee;

    /**
     * 申请退款金额,单位为分
     */
    private BigDecimal settleFee;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public BigDecimal getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }

    public BigDecimal getSettleFee() {
        return settleFee;
    }

    public void setSettleFee(BigDecimal settleFee) {
        this.settleFee = settleFee;
    }

    public OrderRefundPay() {
    }

    public OrderRefundPay(String tradeNo, BigDecimal refundFee, BigDecimal settleFee) {
        this.tradeNo = tradeNo;
        this.refundFee = refundFee;
        this.settleFee = settleFee;
    }
}
