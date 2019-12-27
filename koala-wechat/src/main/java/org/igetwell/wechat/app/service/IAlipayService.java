package org.igetwell.wechat.app.service;

import javax.servlet.http.HttpServletRequest;

public interface IAlipayService {

    /**
     * 手机网站支付预付款下单
     * @param productName
     * @param productId
     * @param fee
     * @return
     */
    String wapPay(String subject, String productName, String productId, String fee) throws Exception;

    /**
     * PC网站支付
     * @param subject
     * @param productName
     * @param productId
     * @param fee
     * @return
     * @throws Exception
     */
    String webPc(String subject, String productName, String productId, String fee) throws Exception;

    /**
     * 扫码预付款下单
     * @param subject
     * @param productName
     * @param productId
     * @param fee
     * @return
     * @throws Exception
     */
    String scanPay(String subject, String productName, String productId, String fee) throws Exception;

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
