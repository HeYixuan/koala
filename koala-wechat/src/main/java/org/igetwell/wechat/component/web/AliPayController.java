package org.igetwell.wechat.component.web;

import org.igetwell.wechat.BaseController;
import org.igetwell.wechat.app.service.IAlipayRefundService;
import org.igetwell.wechat.app.service.IAlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alipay/pay")
public class AliPayController extends BaseController {

    @Autowired
    private IAlipayService iAlipayService;

    @Autowired
    private IAlipayRefundService iAlipayRefundService;

    /**
     * 支付宝支付返回通知
     */
    @PostMapping(value = "/payNotify")
    public void payNotify() {
        String text = iAlipayService.notifyMethod(request.get());
        render(text);
    }


    /**
     * 微信支付返回通知
     */
    @PostMapping(value = "/refundNotify")
    public void refundNotify(){
        String resultXml = iAlipayRefundService.notifyMethod(request.get());
        renderXml(resultXml);
    }

    @PostMapping("/refundPay")
    public void returnPay(String transactionId, String tradeNo, String outNo) throws Exception {
        iAlipayRefundService.returnPay(transactionId, tradeNo, outNo, "0.01");
    }
}
