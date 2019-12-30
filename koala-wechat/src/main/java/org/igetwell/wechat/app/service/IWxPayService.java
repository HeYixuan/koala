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
     * @param productName
     * @param productId
     * @param fee
     * @return
     */
    Map<String, String> preOrder(HttpServletRequest request, String openId, TradeType tradeType, String productName, String productId, String fee);

    /**
     * 处理微信支付回调
     * @param xmlStr
     * @return
     */
    String notifyMethod(String xmlStr);
}
