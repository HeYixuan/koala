package org.igetwell.wechat.app.service.impl;

import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.enums.OrderStatus;
import org.igetwell.common.enums.OrderType;
import org.igetwell.common.enums.TradeType;
import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.BigDecimalUtils;
import org.igetwell.common.uitls.CharacterUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.common.uitls.WebUtils;
import org.igetwell.system.order.dto.request.ComponentPayRequest;
import org.igetwell.system.order.entity.TradeOrder;
import org.igetwell.system.order.feign.TradeOrderClient;
import org.igetwell.wechat.app.service.IAlipayService;
import org.igetwell.wechat.app.service.IPayPalService;
import org.igetwell.wechat.app.service.IWxPayService;
import org.igetwell.wechat.component.service.IWxComponentAppService;
import org.igetwell.wechat.sdk.bean.component.ComponentAppAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class PayPalService implements IPayPalService {

    private static final Logger logger = LoggerFactory.getLogger(PayPalService.class);

    @Autowired
    private IWxComponentAppService iWxComponentAppService;
    @Autowired
    private IWxPayService iWxPayService;
    @Autowired
    private IAlipayService iAlipayService;

    @Autowired
    private Sequence sequence;

    @Autowired
    private TradeOrderClient tradeOrderClient;


    @Override
    public ResponseEntity paypal(HttpServletRequest request, ComponentPayRequest payRequest) throws Exception {
        logger.info("[统一下单支付接口服务]-请求统一下单支付接口开始.");
        if (!this.checkParams(payRequest)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "请求参数不能为空.");
        }
        if (BigDecimalUtils.lessThan(payRequest.getFee(), new BigDecimal(0))){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "订单金额必须大于0");
        }
        if(WebUtils.isWechat()) { //微信
            TradeOrder orders = new TradeOrder(sequence.nextValue(), sequence.nextNo(), payRequest.getMchId(), payRequest.getMchNo(), OrderType.CHARGE.getValue(),
                    payRequest.getChannelId(), payRequest.getFee(), WebUtils.getIP(), OrderStatus.PENDING.getValue(), payRequest.getGoodsId(), payRequest.getBody());
            tradeOrderClient.saveOrder(orders);
            ComponentAppAccessToken accessToken = iWxComponentAppService.getAccessToken("wx2681cc8716638f35", "oNDnvs8I7ewNZrB6iFZC4s7Fxn88");
            int fee = BigDecimalUtils.multiply(orders.getFee(), new BigDecimal(100)).intValue();
            Map<String, String> resultMap = iWxPayService.preOrder(request, accessToken.getOpenid(), TradeType.JSAPI,payRequest.getBody(), payRequest.getGoodsId().toString(), String.valueOf(fee));
            return ResponseEntity.ok(resultMap);

        } else {  //支付宝
            TradeOrder orders = new TradeOrder(sequence.nextValue(), sequence.nextNo(), payRequest.getMchId(), payRequest.getMchNo(), OrderType.CHARGE.getValue(),
                    payRequest.getChannelId(), payRequest.getFee(), WebUtils.getIP(), OrderStatus.PENDING.getValue(), payRequest.getGoodsId(), payRequest.getBody());
            tradeOrderClient.saveOrder(orders);
            //String page = iAlipayService.wapPay("cp21", "cp001", "Gw100001", "0.01");
            Map<String, String> resultMap = iAlipayService.scanPay(payRequest.getGoodsId().toString(), payRequest.getBody(), String.valueOf(orders.getFee()));
            return ResponseEntity.ok(resultMap);
        }
        //return ResponseEntity.ok("其它支付");
    }

    private boolean checkParams(ComponentPayRequest payRequest) {
        if (StringUtils.isEmpty(payRequest)) {
            return false;
        }
        if (payRequest.getMchId() == null || CharacterUtils.isBlank(payRequest.getMchNo())) {
            return false;
        }
        if (payRequest.getGoodsId() == null || CharacterUtils.isBlank(payRequest.getMchNo())) {
            return false;
        }
        if (StringUtils.isEmpty(payRequest.getFee())) {
            logger.info("[统一下单支付接口服务]-支付金额不能为空.");
            return false;
        }
        return true;
    }
}
