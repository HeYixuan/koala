package org.igetwell.wechat.component.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.common.uitls.WebUtils;
import org.igetwell.system.bean.dto.request.AliPayRequest;
import org.igetwell.wechat.BaseController;
import org.igetwell.wechat.app.service.IAlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/ali/pay")
public class AliPayController extends BaseController {

    @Autowired
    private IAlipayService iAlipayService;

    @PostMapping("/aliPay")
    public ResponseEntity<Map<String, String>> wxPay(@RequestBody AliPayRequest payRequest) {
        Map<String, String> packageMap = iAlipayService.preOrder(payRequest);
        return ResponseEntity.ok(packageMap);
    }

    /**
     * 支付宝支付返回通知
     */
    @PostMapping(value = "/payNotify")
    public void payNotify() {
        String text = iAlipayService.notifyMethod(request.get());
        render(text);
    }

    @PostMapping("/refundPay")
    public void returnPay(String transactionId, String tradeNo, String outNo) throws Exception {
        iAlipayService.returnPay(transactionId, tradeNo, outNo, "0.01");
    }

    @GetMapping("/common/pay")
    public ResponseEntity commonPay(@RequestParam("amount") BigDecimal amount) throws Exception {
        if(WebUtils.isWechat()){ //微信
            //ComponentAppAccessToken accessToken = iWxComponentAppService.getAccessToken("wx2681cc8716638f35", "oNDnvs8I7ewNZrB6iFZC4s7Fxn88");
            //Map<String, String> resultMap = iWxPayService.preOrder(request.get(), accessToken.getOpenid(), TradeType.JSAPI,"官网费用","GW201807162055", String.valueOf(amount));
            //return ResponseEntity.ok(resultMap);
        } else if (WebUtils.isAliPay()) {  //支付宝
            //String page = iAlipayService.wapPay("cp21", "cp001", "Gw100001", "0.01");
            Map<String, String> resultMap = iAlipayService.scan("1231131","cp21", "cp001","0.01");
            return ResponseEntity.ok(resultMap);
        }
        return ResponseEntity.ok("其它支付");
    }

    @GetMapping("/alipay/pay")
    public ResponseEntity alipay(@RequestParam("amount") BigDecimal amount) throws Exception {
        //手机网站支付
        //String page = iAlipayService.wapPay("cp21", "cp001", "Gw100001", "0.01");
        //PC网站支付
        //String page = iAlipayService.webPc("cp21", "cp001", "Gw100001", "0.01");
        //面对面扫码支付
        Map<String, String> resultMap = iAlipayService.scan("1231131","cp21", "cp001","0.01");
        return ResponseEntity.ok(resultMap);
    }

    /**
     * 测试扫码支付
     * @param amount
     * @return
     * @throws Exception
     */
    @GetMapping("/alipay/pay/pay")
    public ModelAndView pay(@RequestParam("amount") BigDecimal amount) throws Exception {
        //手机网站支付
        Map<String, String> resultMap = iAlipayService.wap("1231311","cp21", "cp001", "0.01");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("page", resultMap);
        modelAndView.setViewName("/success");
        return modelAndView;
    }
}
