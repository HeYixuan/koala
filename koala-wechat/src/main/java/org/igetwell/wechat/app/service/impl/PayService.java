package org.igetwell.wechat.app.service.impl;

import org.igetwell.common.enums.TradeType;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.common.uitls.WebUtils;
import org.igetwell.system.order.entity.TradeOrder;
import org.igetwell.system.order.feign.TradeOrderClient;
import org.igetwell.wechat.app.service.IAlipayService;
import org.igetwell.wechat.app.service.IPayService;
import org.igetwell.wechat.app.service.IWxPayService;
import org.igetwell.wechat.component.service.IWxComponentAppService;
import org.igetwell.wechat.sdk.bean.component.ComponentAppAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class PayService implements IPayService {


    @Autowired
    private IWxComponentAppService iWxComponentAppService;
    @Autowired
    private IWxPayService iWxPayService;
    @Autowired
    private IAlipayService iAlipayService;

    @Autowired
    private TradeOrderClient tradeOrderClient;


    @Override
    public ResponseEntity componentPay(HttpServletRequest request, String amount) throws Exception {
        TradeOrder orders = new TradeOrder();
        orders.setId(12313131311L);
        orders.setClientIp(WebUtils.getIP());
        orders.setTradeNo("STG1221545513131");
        orders.setFee(new BigDecimal(amount));
        orders.setOrderType(1);//充值
        orders.setStatus(1);//订单生成
        orders.setMchId(1L);
        orders.setMchNo("NO:001");
        orders.setBody("商品描述");
        if(WebUtils.isWechat(request)){ //微信
            ComponentAppAccessToken accessToken = iWxComponentAppService.getAccessToken("wx2681cc8716638f35", "oNDnvs8I7ewNZrB6iFZC4s7Fxn88");
            Map<String, String> resultMap = iWxPayService.preOrder(request, accessToken.getOpenid(), TradeType.JSAPI,"官网费用","GW201807162055", String.valueOf(amount));
            orders.setChannelId(1L);//微信
            return ResponseEntity.ok(resultMap);

        } else if (WebUtils.isAliPay(request)) {  //支付宝
            //String page = iAlipayService.wapPay("cp21", "cp001", "Gw100001", "0.01");
            orders.setChannelId(2L);//支付宝

            String qrUrl = iAlipayService.scanPay("cp21", "cp001", "Gw100001", "0.01");
            return ResponseEntity.ok(qrUrl);
        }
        return ResponseEntity.ok("其它支付");
    }
}
