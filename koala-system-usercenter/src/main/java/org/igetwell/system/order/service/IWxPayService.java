package org.igetwell.system.order.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.dto.request.OrderPayRequest;

public interface IWxPayService {

    /**
     * 微信支付成功回调,开始向MQ发送消息下单
     * @return
     */
    ResponseEntity createOrder(OrderPayRequest request);
}
