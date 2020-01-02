package org.igetwell.paypal.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.paypal.dto.request.PayPalRequest;
import org.igetwell.paypal.service.impl.PayPalService;
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

    @PostMapping("/aliPay")
    public ResponseEntity<Map<String, String>> aliPay(@RequestBody PayPalRequest payPalRequest) {
        return payPalService.aliPay(payPalRequest);
    }

    /**
     * 公众号支付，APP内调起微信支付
     * 微信H5、APP内调起支付
     * @return
     */
    @PostMapping("/wxPay")
    public ResponseEntity<Map<String, String>> wxPay(@RequestBody PayPalRequest payPalRequest) {
        return payPalService.wxPay(payPalRequest);
    }
}
