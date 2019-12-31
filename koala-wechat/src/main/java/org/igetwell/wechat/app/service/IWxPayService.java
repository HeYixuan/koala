package org.igetwell.wechat.app.service;

import org.igetwell.common.enums.TradeType;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 微信支付
 */
public interface IWxPayService {

    /**
     * 扫码预付款下单
     * @param request
     * @param productName
     * @param productId
     * @param fee
     * @return
     */
    String scanOrder(HttpServletRequest request, String productName, String productId, String fee);

    /**
     * APP预付款下单
     * @param request
     * @param productName
     * @param productId
     * @param fee
     * @return
     */
    Map<String, String> jsAppOrder(HttpServletRequest request, String productName, String productId, String fee);

    /**
     * 微信JSAPI、H5、APP、NATIVE调起支付
     * @param request
     * @param tradeType
     * @param body
     * @param productId
     * @param fee
     * @return
     */
    Map<String, String> preOrder(HttpServletRequest request, String openId, TradeType tradeType, String body, String productId, String fee);

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
    void refundPay(String transactionId, String tradeNo, String outNo, String totalFee, String fee) throws Exception;

    /**
     * 处理微信退款回调
     * @param xmlStr
     * @return
     */
    String refundNotify(String xmlStr);
}
