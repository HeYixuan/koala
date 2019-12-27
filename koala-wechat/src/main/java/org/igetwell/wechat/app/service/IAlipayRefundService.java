package org.igetwell.wechat.app.service;

import javax.servlet.http.HttpServletRequest;

public interface IAlipayRefundService {

    /**
     * 支付宝退款
     * @param transactionId 支付宝订单号
     * @param tradeNo 商户订单号
     * @param fee
     * @throws Exception
     */
    void returnPay(String transactionId, String tradeNo, String outNo, String fee) throws Exception;

    /**
     * 处理支付宝退款回调
     * @return
     */
    String notifyMethod(HttpServletRequest request);
}
