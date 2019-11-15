package org.igetwell.system.member.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 下单sdk接口返回参数
 */
@Getter
@Setter
public class ChargeOrderResponse implements Serializable {

    private static final long serialVersionUID = -5685058946404699059L;

    /**秒杀订单号*/
    private String orderNo;
    /**用户下单手机号*/
    private String mobile;
    /**商品id*/
    private String prodId;
    /**用户交易金额*/
    private BigDecimal money;
}
