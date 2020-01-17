package org.igetwell.wechat.app.service;

import org.igetwell.common.enums.TradeType;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.bean.dto.request.WxPayRequest;
import org.igetwell.system.bean.dto.request.WxRefundRequest;

import java.util.Map;

/**
 * 微信支付
 */
public interface IWxPayService {

    /**
     * 扫码预付款下单
     */
    Map<String, String> scan(String tradeNo, String productId, String body, String fee, String clientIp);

    /**
     * JSAPI预付款下单
     */
    Map<String, String> jsapi(String openId, String tradeNo, String body, String fee, String clientIp);

    /**
     * APP预付款下单
     */
    Map<String, String> app(String tradeNo, String body, String fee, String clientIp);

    /**
     * PC网站预付款下单
     */
    Map<String, String> web(String tradeNo, String body, String fee, String clientIp);

    /**
     * 微信JSAPI、H5、APP、NATIVE调起支付
     */
    Map<String, String> preOrder(TradeType tradeType, String openId, String productId, String body, String fee, String clientIp);

    /**
     * 预支付
     */
    Map<String, String> preOrder(WxPayRequest payRequest);

    /**
     * 处理微信支付回调
     * @param xmlStr
     * @return
     */
    String payNotify(String xmlStr);

    /**
     * 微信退款
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @param fee
     * @throws Exception
     */
    void refund(String transactionId, String tradeNo, String outNo, String totalFee, String fee);

    /**
     * 微信退款
     */
    void refund(WxRefundRequest refundRequest);

    /**
     * 处理微信退款回调
     * @param xmlStr
     * @return
     */
    String refundNotify(String xmlStr);

    /**
     * 查询支付订单
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @return
     */
    ResponseEntity getOrder(String transactionId, String tradeNo);

    /**
     * 关闭订单
     * @param tradeNo 商户订单号
     * @return
     */
    ResponseEntity closeOrder(String tradeNo);
}
