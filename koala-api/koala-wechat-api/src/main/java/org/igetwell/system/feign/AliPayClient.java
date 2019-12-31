package org.igetwell.system.feign;

import org.igetwell.system.bean.dto.request.WxPayRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "koala-wechat")
public interface AliPayClient {

    /**
     * 公众号支付，APP内调起微信支付
     * 微信H5、APP内调起支付
     * @return
     */
    @PostMapping("/wx/pay/jsPay")
    public Map<String, String> jsPay(@RequestBody WxPayRequest wxPayRequest);
}
