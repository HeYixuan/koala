package org.igetwell.system.feign;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.bean.dto.request.AliPayRequest;
import org.igetwell.system.bean.dto.request.AliRefundRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "koala-wechat")
public interface AliPayClient {


    @PostMapping("/ant/pay/antPay")
    ResponseEntity<Map<String, String>> antPay(@RequestBody AliPayRequest aliPayRequest);

    @PostMapping("/ant/pay/antRefund")
    ResponseEntity refund(@RequestBody AliRefundRequest refundRequest);
}
