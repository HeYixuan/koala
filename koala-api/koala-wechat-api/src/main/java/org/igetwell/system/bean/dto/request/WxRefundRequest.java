package org.igetwell.system.bean.dto.request;

import java.math.BigDecimal;

public class WxRefundRequest extends RefundRequest {

    public WxRefundRequest() {
    }

    public WxRefundRequest(String tradeNo, String transactionId, String outNo, BigDecimal totalFee, BigDecimal fee) {
        super(tradeNo, transactionId, outNo, totalFee, fee);
    }
}
