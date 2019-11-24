package org.igetwell.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayType {
    //支付状态：-1订单超时 0待支付 1支付成功 2待退款 3退款完成
    TIMEOUT(-1),
    PENDING(0),
    PAID(1),
    PEND_REFUND(2),
    REFUND(3);

    private int status;

}
