package org.igetwell.paypal.service.impl;

import org.igetwell.paypal.dto.request.PayPalRequest;
import org.igetwell.paypal.service.IPayPalService;
import org.igetwell.system.bean.dto.request.AliPayRequest;
import org.igetwell.system.bean.dto.request.WxPayRequest;
import org.igetwell.system.feign.PayPalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PayPalService implements IPayPalService {

    @Autowired
    private PayPalClient payPalClient;

    public Map<String, String> aliPay(PayPalRequest payPalRequest) {
        AliPayRequest aliPayRequest = new AliPayRequest();
        aliPayRequest.setGoodsId(payPalRequest.getGoodsId());
        aliPayRequest.setBody(payPalRequest.getBody());
        aliPayRequest.setFee(payPalRequest.getFee());
        return payPalClient.aliPay(aliPayRequest);
    }

    /**
     * 公众号支付，APP内调起微信支付
     * 微信H5、APP内调起支付
     * @return
     */
    public Map<String, String> wxPay(PayPalRequest payPalRequest) {
        WxPayRequest wxPayRequest = new WxPayRequest();
        wxPayRequest.setOpenId(payPalRequest.getOpenId());
        wxPayRequest.setTradeType(payPalRequest.getTradeType());
        wxPayRequest.setGoodsId(payPalRequest.getGoodsId());
        wxPayRequest.setBody(payPalRequest.getBody());
        wxPayRequest.setFee(payPalRequest.getFee());
        return payPalClient.wxPay(wxPayRequest);
    }
}
