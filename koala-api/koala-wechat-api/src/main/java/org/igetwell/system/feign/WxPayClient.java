package org.igetwell.system.feign;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.bean.dto.request.WxPayRequest;
import org.igetwell.system.bean.dto.request.WxRefundRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "koala-wechat")
public interface WxPayClient {

    /**
     * 公众号支付，APP内调起微信支付
     * 微信H5、APP内调起支付
     * @return
     */
    @PostMapping("/wx/pay/wxPay")
    ResponseEntity<Map<String, String>> wxPay(@RequestBody WxPayRequest payRequest);

    @PostMapping("/wx/pay/wxRefund")
    ResponseEntity<Map<String, String>> refund(@RequestBody WxRefundRequest refundRequest);
}
