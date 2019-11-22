package org.igetwell.system.member.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.member.dto.request.ChargeOrderRequest;

public interface IMemberOrderService {

    /**
     * 用户下单
     *  1.下单参数校验
     *  2.前置商品校验
     *  3.预减库存
     *  4.入队发送MQ消息
     * @return
     */
    ResponseEntity createOrder(ChargeOrderRequest chargeOrderRequest);

    ResponseEntity createOrderQueue(ChargeOrderRequest chargeOrderRequest);

}
