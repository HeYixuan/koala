package org.igetwell.system.member.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 下单接口请求参数
 */

@Getter
@Setter
public class ChargeOrderRequest implements Serializable {

    private static final long serialVersionUID = 2596328097263464531L;

    /**充值手机号*/
    private String mobile;
    /**充值金额*/
    private BigDecimal money;
    /**商品id*/
    private String prodId;

}
