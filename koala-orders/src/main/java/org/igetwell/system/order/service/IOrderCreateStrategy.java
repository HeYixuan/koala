package org.igetwell.system.order.service;


import org.igetwell.common.uitls.ResponseEntity;

import java.math.BigDecimal;

public interface IOrderCreateStrategy {
    /**
     * 创建订单(工厂模式)
     * @return
     */
    ResponseEntity createOrder(BigDecimal money);
}
