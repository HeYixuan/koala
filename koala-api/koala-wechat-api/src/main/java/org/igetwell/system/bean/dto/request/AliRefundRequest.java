package org.igetwell.system.bean.dto.request;

import java.math.BigDecimal;

public class AliRefundRequest extends RefundRequest {

    public AliRefundRequest() {
    }

    public AliRefundRequest(String tradeNo, String transactionId, String outNo, BigDecimal totalFee, BigDecimal fee) {
        super(tradeNo, transactionId, outNo, totalFee, fee);
    }
}
