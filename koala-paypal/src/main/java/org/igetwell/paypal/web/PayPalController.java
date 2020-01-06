package org.igetwell.paypal.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.paypal.dto.request.PayPalRefundRequest;
import org.igetwell.paypal.dto.request.PayPalRequest;
import org.igetwell.paypal.service.impl.PayPalService;
import org.igetwell.system.bean.dto.request.AliRefundRequest;
import org.igetwell.system.bean.dto.request.WxRefundRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/component/pay")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/wxPay")
    public ResponseEntity<Map<String, String>> wxPay(@RequestBody PayPalRequest payPalRequest) {
        return payPalService.wxPay(payPalRequest);
    }


    @PostMapping("/refund")
    public ResponseEntity refund(@RequestBody PayPalRefundRequest refundRequest){
        return payPalService.refund(refundRequest);
    }
}
