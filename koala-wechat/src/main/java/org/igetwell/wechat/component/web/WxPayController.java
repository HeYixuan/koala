package org.igetwell.wechat.component.web;

import org.igetwell.common.enums.TradeType;
import org.igetwell.common.uitls.IOUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.common.uitls.WebUtils;
import org.igetwell.wechat.BaseController;
import org.igetwell.wechat.app.service.IAlipayService;
import org.igetwell.wechat.component.service.ILocalPayService;
import org.igetwell.wechat.component.service.ILocalReturnPayService;
import org.igetwell.wechat.component.service.IWxComponentAppService;
import org.igetwell.wechat.sdk.bean.component.ComponentAppAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/wx/pay")
public class WxPayController extends BaseController {

    @Autowired
    private ILocalPayService localPayService;
    @Autowired
    private ILocalReturnPayService localReturnPayService;

    @Autowired
    private IWxComponentAppService iWxComponentAppService;

    @Autowired
    private IAlipayService iAlipayService;

    /**
     * 扫码支付
     * @return
     */
    @PostMapping("/scanPay")
    public String scanPay() {
        String codeUrl = localPayService.scanOrder(request.get(), "官网费用","GW201807162055","1");
        return codeUrl;
    }


    /**
     * 公众号支付，APP内调起微信支付
     * 微信H5、APP内调起支付
     * @return
     */
    @PostMapping("/preOrder")
    public Map<String, String> preOrder() {
        return localPayService.preOrder(request.get(), "ojhc61KyGnCepGMIpcZI-YCPce30", TradeType.MWEB,"官网费用","GW201807162055","1");
    }

    /**
     * 微信退款
     */
    @PostMapping("/returnPay")
    public ResponseEntity returnPay(String transactionId, String tradeNo, String fee) throws Exception {
        localReturnPayService.returnPay(transactionId, tradeNo,"1", "1", fee);
        return ResponseEntity.ok();
    }


    /**
     * 微信支付返回通知
     */
    @PostMapping(value = "/payNotify", produces = {"application/xml"})
    public void payNotify(){
        String xmlStr = IOUtils.readData(request.get());
        String resultXml = localPayService.notifyMethod(xmlStr);
        renderXml(resultXml);
    }

    /**
     * 微信支付返回通知
     */
    @PostMapping(value = "/refundNotify", produces = {"application/xml"})
    public void refundNotify(){
        String xmlStr = IOUtils.readData(request.get());
        String resultXml = localReturnPayService.notifyMethod(xmlStr);
        renderXml(resultXml);
    }

    @GetMapping("/common/pay")
    public ResponseEntity commonPay(@RequestParam("amount") BigDecimal amount) throws Exception {
        if(WebUtils.isWechat(request.get())){ //微信
            ComponentAppAccessToken accessToken = iWxComponentAppService.getAccessToken("wx2681cc8716638f35", "oNDnvs8I7ewNZrB6iFZC4s7Fxn88");
            Map<String, String> resultMap = localPayService.preOrder(request.get(), accessToken.getOpenid(), TradeType.JSAPI,"官网费用","GW201807162055", String.valueOf(amount));
            return ResponseEntity.ok(resultMap);
        } else if (WebUtils.isAliPay(request.get())) {  //支付宝
            //String page = iAlipayService.wapPay("cp21", "cp001", "Gw100001", "0.01");
            String page = iAlipayService.scanPay("cp21", "cp001", "Gw100001", "0.01");
            return ResponseEntity.ok(page);
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
        String page = iAlipayService.scanPay("cp23", "cp002", "Gw100002", "0.03");
        return ResponseEntity.ok(page);
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
        String page = iAlipayService.wapPay("cp21", "cp001", "Gw100001", "0.01");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("page", page);
        modelAndView.setViewName("/success");
        return modelAndView;
    }

}
