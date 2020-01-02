package org.igetwell.system.feign;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.bean.dto.request.AliPayRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "koala-wechat")
public interface AliPayClient {


    @PostMapping("/ali/pay/aliPay")
    ResponseEntity<Map<String, String>> aliPay(@RequestBody AliPayRequest aliPayRequest);
}
