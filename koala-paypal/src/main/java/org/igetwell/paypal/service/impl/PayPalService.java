package org.igetwell.paypal.service.impl;

import org.igetwell.common.enums.PayChannel;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.paypal.dto.request.PayPalRefundRequest;
import org.igetwell.paypal.dto.request.PayPalRequest;
import org.igetwell.paypal.service.IPayPalService;
import org.igetwell.system.bean.dto.request.AliPayRequest;
import org.igetwell.system.bean.dto.request.AliRefundRequest;
import org.igetwell.system.bean.dto.request.WxPayRequest;
import org.igetwell.system.bean.dto.request.WxRefundRequest;
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


    public ResponseEntity<Map<String, String>> wxPay(PayPalRequest payPalRequest) {
        if (payPalRequest.getChannel().equals(PayChannel.WECHAT)){
            WxPayRequest payRequest = new WxPayRequest(payPalRequest.getTradeType(), payPalRequest.getTradeNo(), payPalRequest.getProductId(),
                    payPalRequest.getBody(), payPalRequest.getFee(), payPalRequest.getClientIp());
            return wxPayClient.wxPay(payRequest);
        } else {
            AliPayRequest payRequest = new AliPayRequest(payPalRequest.getTradeType(), payPalRequest.getTradeNo(), payPalRequest.getProductId(),
                    payPalRequest.getBody(), payPalRequest.getFee());
            return aliPayClient.antPay(payRequest);
        }
    }

    public ResponseEntity refund(PayPalRefundRequest refundRequest) {
        if (refundRequest.getChannel().equals(PayChannel.WECHAT)){
            WxRefundRequest payRequest = new WxRefundRequest(refundRequest.getTradeNo(), refundRequest.getTransactionId(), refundRequest.getOutNo(), refundRequest.getTotalFee(), refundRequest.getFee());
            return wxPayClient.refund(payRequest);
        } else {
            AliRefundRequest payRequest = new AliRefundRequest(refundRequest.getTradeNo(), refundRequest.getTransactionId(), refundRequest.getOutNo(), refundRequest.getTotalFee(), refundRequest.getFee());
            return aliPayClient.refund(payRequest);
        }
    }
}
