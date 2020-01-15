package org.igetwell.paypal.feign;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.paypal.dto.request.PayPalRefundRequest;
import org.igetwell.paypal.dto.request.PayPalRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "koala-paypal")
public interface PayPalClient {

    @PostMapping("/component/pay/scan")
    ResponseEntity<Map<String, String>> scan(@RequestBody PayPalRequest payPalRequest);

    @PostMapping("/component/pay/refund")
    ResponseEntity refund(@RequestBody PayPalRefundRequest request);
}
