package org.igetwell.wechat.component.service;

/**
 * 微信退款
 */
public interface ILocalReturnPayService {

    /**
     * 微信退款
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @param fee
     * @throws Exception
     */
    void returnPay(String transactionId, String tradeNo, String outNo, String totalFee, String fee) throws Exception;

    /**
     * 处理微信退款回调
     * @param xmlStr
     * @return
     */
    String notifyMethod(String xmlStr);
}
