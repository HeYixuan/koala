package org.igetwell.paypal.service.impl;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.paypal.dto.request.PayPalRequest;
import org.igetwell.paypal.service.IPayPalService;
import org.igetwell.system.bean.dto.request.AliPayRequest;
import org.igetwell.system.bean.dto.request.WxPayRequest;
import org.igetwell.system.feign.AliPayClient;
import org.igetwell.system.feign.WxPayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PayPalService implements IPayPalService {

    @Autowired
    private WxPayClient wxPayClient;
    @Autowired
    private AliPayClient aliPayClient;

    /**
     * 支付宝支付
     * @param payPalRequest
     * @return
     */
    public ResponseEntity<Map<String, String>> aliPay(PayPalRequest payPalRequest) {
        AliPayRequest payRequest = new AliPayRequest(payPalRequest.getTradeType(), payPalRequest.getTradeNo(), payPalRequest.getProductId(),
                payPalRequest.getBody(), payPalRequest.getFee());
        return aliPayClient.aliPay(payRequest);
    }

    /**
     * 公众号支付，APP内调起微信支付
     * 微信H5、APP内调起支付
     * @return
     */
    public ResponseEntity<Map<String, String>> wxPay(PayPalRequest payPalRequest) {
        WxPayRequest payRequest = new WxPayRequest(payPalRequest.getTradeType(), payPalRequest.getTradeNo(), payPalRequest.getProductId(),
                payPalRequest.getBody(), payPalRequest.getFee(), payPalRequest.getClientIp());
        return wxPayClient.wxPay(payRequest);
    }
}
