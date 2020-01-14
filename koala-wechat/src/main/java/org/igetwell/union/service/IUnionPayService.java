package org.igetwell.union.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IUnionPayService {

    /**
     * 手机网站支付预付款下单
     */
    Map<String, String> wap(String tradeNo, String subject, String body, String fee);

    /**
     * PC网站支付
     */
    Map<String, String> web(String tradeNo, String subject, String body, String fee);

    /**
     * 商户统一下单(用户主扫)
     */
    Map<String, String> unifiedOrder(String tradeNo, String productId, String body, String fee);

    /**
     * 扫码预付款下单(用户主扫)
     */
    Map<String, String> scan(String tradeNo, String productId, String body, String fee);

    /**
     * 银联支付回调
     * @return
     */
    String payNotify(HttpServletRequest request);

    /**
     * 银联退款
     * @param transactionId
     * @param outTradeNo
     * @param fee
     */
    void refund(String transactionId, String tradeNo, String outTradeNo, String fee);

    /**
     * 银联退款回调
     * @return
     */
    String refundNotify(HttpServletRequest request);
}
