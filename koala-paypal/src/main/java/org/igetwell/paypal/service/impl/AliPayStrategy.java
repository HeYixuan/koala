package org.igetwell.paypal.service.impl;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.paypal.dto.request.PayPalRefundRequest;
import org.igetwell.paypal.dto.request.PayPalRequest;
import org.igetwell.paypal.service.IPayStrategy;

import org.igetwell.system.bean.dto.request.AliPayRequest;
import org.igetwell.system.bean.dto.request.AliRefundRequest;
import org.igetwell.system.feign.AliPayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AliPayStrategy implements IPayStrategy {

    @Autowired
    private AliPayClient aliPayClient;

    @Override
    public ResponseEntity<Map<String, String>> scan(PayPalRequest request) {
        AliPayRequest payRequest = new AliPayRequest(request.getTradeType(), request.getTradeNo(), request.getProductId(), request.getBody(), request.getFee());
        return aliPayClient.antPay(payRequest);
    }

    @Override
    public ResponseEntity refund(PayPalRefundRequest request) {
        AliRefundRequest payRequest = new AliRefundRequest(request.getTradeNo(), request.getTransactionId(), request.getOutNo(), request.getTotalFee(), request.getFee());
        return aliPayClient.refund(payRequest);
    }

}
