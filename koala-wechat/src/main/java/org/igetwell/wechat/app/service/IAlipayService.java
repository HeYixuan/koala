package org.igetwell.wechat.app.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IAlipayService {

    /**
     * 手机网站支付预付款下单
     * @param body
     * @param fee
     * @return
     */
    String wapPay(String subject, String body, String fee) throws Exception;

    /**
     * PC网站支付
     * @param subject
     * @param body
     * @param fee
     * @return
     * @throws Exception
     */
    String webPc(String subject, String body, String fee) throws Exception;

    /**
     * 扫码预付款下单
     * @param subject
     * @param body
     * @param fee
     * @return
     * @throws Exception
     */
    Map<String, String> scanPay(String subject, String body, String fee);


    /**
     * 支付宝退款
     * @param transactionId 支付宝订单号
     * @param tradeNo 商户订单号
     * @param fee
     * @throws Exception
     */
    void returnPay(String transactionId, String tradeNo, String outNo, String fee) throws Exception;

    /**
     * 处理支付宝支付回调
     * @return
     */
    String notifyMethod(HttpServletRequest request);

    /**
     * 处理支付宝服务器同步通知
     * @return
     */
    String returnMethod(HttpServletRequest request);
}
