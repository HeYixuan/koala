package org.igetwell.paypal.service.impl;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.paypal.dto.request.PayPalRefundRequest;
import org.igetwell.paypal.dto.request.PayPalRequest;
import org.igetwell.paypal.service.IPayStrategy;
import org.igetwell.system.bean.dto.request.WxPayRequest;
import org.igetwell.system.bean.dto.request.WxRefundRequest;
import org.igetwell.system.feign.WxPayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WxPayStrategy implements IPayStrategy {

    @Autowired
    private WxPayClient wxPayClient;

    @Override
    public ResponseEntity<Map<String, String>> scan(PayPalRequest request) {
        WxPayRequest payRequest = new WxPayRequest(request.getTradeType(), request.getTradeNo(), request.getProductId(),
                request.getBody(), request.getFee(), request.getClientIp());
        return wxPayClient.wxPay(payRequest);

    }

    @Override
    public ResponseEntity refund(PayPalRefundRequest request) {
        WxRefundRequest payRequest = new WxRefundRequest(request.getTradeNo(), request.getTransactionId(), request.getOutNo(), request.getTotalFee(), request.getFee());
        return wxPayClient.refund(payRequest);
    }
}
