package org.igetwell.system.member.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.member.dto.request.ChargeOrderRequest;

public interface IOrderService {

    /**
     * 用户秒杀下单
     * 1.接口验签防刷
     * 2.前置参数校验
     * 3.商品校验,库存校验
     * 4.预减库存,减库存成功,根据订单号缓存30分钟并生成订单记录,提示用户支付
     * 5.用户未支付库存增加,支付成功回调发送MQ消息入队,消费者减真实库存
     * @param chargeOrderRequest
     * @return
     */
    ResponseEntity createOrder(ChargeOrderRequest chargeOrderRequest);


    /**
     * 订单入队,缓存30分钟并生成订单记录,提示用户支付
     * @param chargeOrderRequest
     * @return
     */
    ResponseEntity createOrderQueue(ChargeOrderRequest chargeOrderRequest);

    /**
     * 从缓存预减库存
     * @param prodId
     * @return
     */
    boolean reduceStock(String prodId);


}
