package org.igetwell.union.service;

import org.igetwell.common.uitls.ResponseEntity;

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

    /**
     * 查询支付订单
     * @param transactionId 银联订单号
     * @param tradeNo 商户订单号
     * @return
     */
    ResponseEntity getOrder(String transactionId, String tradeNo);

    /**
     * 关闭订单
     * @param outTradeNo 关闭订单号
     * @param tradeNo 商户订单号
     * @return
     */
    ResponseEntity closeOrder(String outTradeNo, String transactionId, String tradeNo, String fee);
}
