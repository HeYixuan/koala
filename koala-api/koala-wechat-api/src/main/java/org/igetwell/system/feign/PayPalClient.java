package org.igetwell.system.feign;

import org.igetwell.system.bean.dto.request.AliPayRequest;
import org.igetwell.system.bean.dto.request.WxPayRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "koala-wechat")
public interface PayPalClient {

    @PostMapping("/paypal/aliPay")
    Map<String, String> aliPay(@RequestBody AliPayRequest aliPayRequest);

    /**
     * 公众号支付，APP内调起微信支付
     * 微信H5、APP内调起支付
     * @return
     */
    @PostMapping("/paypal/wxPay")
    Map<String, String> wxPay(@RequestBody WxPayRequest wxPayRequest);
}
