package org.igetwell.system.member.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.member.dto.response.ChargeOrderResponse;
import org.igetwell.system.member.service.IWxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付
 */
@RestController
public class PayController {

    @Autowired
    private IWxPayService iWxPayService;

    /**
     * 模拟微信支付回调
     */
    @PostMapping("/api/wxPay")
    public ResponseEntity wxPay(@RequestBody ChargeOrderResponse chargeOrderResponse){

        return iWxPayService.createOrder(chargeOrderResponse);
    }
}
