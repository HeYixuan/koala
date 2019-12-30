package org.igetwell.wechat.component.web;

import org.igetwell.common.enums.TradeType;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.common.uitls.WebUtils;
import org.igetwell.wechat.BaseController;
import org.igetwell.wechat.app.service.IAlipayService;
import org.igetwell.wechat.component.service.IWxComponentAppService;
import org.igetwell.wechat.app.service.IWxPayService;
import org.igetwell.wechat.sdk.bean.component.ComponentAppAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/wx/pay")
public class CommonPayController extends BaseController {

    @Autowired
    private IWxComponentAppService iWxComponentAppService;
    @Autowired
    private IWxPayService iWxPayService;
    @Autowired
    private IAlipayService iAlipayService;

    @GetMapping("/common/pay")
    public ResponseEntity commonPay(@RequestParam("amount") BigDecimal amount) throws Exception {
        if(WebUtils.isWechat(request.get())){ //微信
            ComponentAppAccessToken accessToken = iWxComponentAppService.getAccessToken("wx2681cc8716638f35", "oNDnvs8I7ewNZrB6iFZC4s7Fxn88");
            Map<String, String> resultMap = iWxPayService.preOrder(request.get(), accessToken.getOpenid(), TradeType.JSAPI,"官网费用","GW201807162055", String.valueOf(amount));
            return ResponseEntity.ok(resultMap);
        } else if (WebUtils.isAliPay(request.get())) {  //支付宝
            //String page = iAlipayService.wapPay("cp21", "cp001", "Gw100001", "0.01");
            String qrUrl = iAlipayService.scanPay("cp21", "cp001", "Gw100001", "0.01");
            return ResponseEntity.ok(qrUrl);
        }
        return ResponseEntity.ok("其它支付");
    }
}
