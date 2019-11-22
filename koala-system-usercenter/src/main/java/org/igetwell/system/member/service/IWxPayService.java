package org.igetwell.system.member.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.member.dto.request.ChargeOrderRequest;
import org.igetwell.system.member.dto.response.ChargeOrderResponse;

public interface IWxPayService {

    /**
     * 微信支付成功回调后,开始向MQ发送消息下单
     * @return
     */
    ResponseEntity createOrder(ChargeOrderResponse chargeOrderResponse);
}
