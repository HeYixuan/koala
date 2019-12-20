package org.igetwell.wechat.component.web;

import org.igetwell.common.enums.TradeType;
import org.igetwell.common.uitls.IOUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.wechat.BaseController;
import org.igetwell.wechat.component.service.ILocalPayService;
import org.igetwell.wechat.component.service.ILocalReturnPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/wx/pay")
public class WxPayController extends BaseController {

    @Autowired
    private ILocalPayService localPayService;
    @Autowired
    private ILocalReturnPayService localReturnPayService;

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
    @PostMapping(value = "payNotify", produces = {"application/xml"})
    public void payNotify(){
        String xmlStr = IOUtils.readData(request.get());
        String resultXml = localPayService.notifyMethod(xmlStr);
        renderXml(resultXml);
    }

    /**
     * 微信支付返回通知
     */
    @PostMapping(value = "refundNotify", produces = {"application/xml"})
    public void refundNotify(){
        String xmlStr = IOUtils.readData(request.get());
        String resultXml = localReturnPayService.notifyMethod(xmlStr);
        renderXml(resultXml);
    }

}
