package org.igetwell.wechat.component.web;

import org.igetwell.common.uitls.BigDecimalUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.bean.dto.request.WxPayRequest;
import org.igetwell.system.bean.dto.request.AliPayRequest;
import org.igetwell.system.order.dto.request.ComponentPayRequest;
import org.igetwell.wechat.BaseController;
import org.igetwell.wechat.app.service.IAlipayService;
import org.igetwell.wechat.app.service.IPayPalService;
import org.igetwell.wechat.app.service.IWxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/paypal")
public class PayPalController extends BaseController {

    @Autowired
    private IPayPalService iPayPalService;

    @Autowired
    private IWxPayService iWxPayService;

    @Autowired
    private IAlipayService iAlipayService;

    @PostMapping("/paypal")
    public ResponseEntity paypal(@RequestBody ComponentPayRequest payRequest) throws Exception {
        return iPayPalService.paypal(request.get(), payRequest);
    }

    /**
     * 公众号支付，APP内调起微信支付
     * 微信H5、APP内调起支付
     * @return
     */
    @PostMapping("/wxPay")
    public Map<String, String> wxPay(@RequestBody WxPayRequest wxPayRequest) {
        int fee = BigDecimalUtils.multiply(wxPayRequest.getFee(), new BigDecimal(100)).intValue();
        return iWxPayService.preOrder(request.get(), wxPayRequest.getOpenId(), wxPayRequest.getTradeType(), wxPayRequest.getBody(),wxPayRequest.getGoodsId().toString(), String.valueOf(fee));
    }

    @PostMapping("/aliPay")
    public Map<String, String> aliPay(@RequestBody AliPayRequest aliPayRequest) {
        return iAlipayService.scanPay(aliPayRequest.getGoodsId().toString(), aliPayRequest.getBody(), String.valueOf(aliPayRequest.getFee()));
    }
}
