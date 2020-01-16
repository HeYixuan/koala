package org.igetwell.paypal.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.paypal.dto.request.PayPalRefundRequest;
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

    @PostMapping("/scan")
    public ResponseEntity<Map<String, String>> scan(@RequestBody PayPalRequest payPalRequest) {
        return payPalService.scan(payPalRequest);
    }


    @PostMapping("/refund")
    public ResponseEntity refund(@RequestBody PayPalRefundRequest refundRequest){
        return payPalService.refund(refundRequest);
    }
}
