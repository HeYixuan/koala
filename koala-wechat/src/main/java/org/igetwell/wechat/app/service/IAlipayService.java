package org.igetwell.wechat.app.service;

import org.igetwell.common.enums.TradeType;
import org.igetwell.system.bean.dto.request.AliPayRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IAlipayService {

    /**
     * 手机网站支付预付款下单
     */
    Map<String, String> wap(String tradeNo, String subject, String body, String fee);

    /**
     * PC网站支付
     */
    Map<String, String> web(String tradeNo, String subject, String body, String fee);

    /**
     * 扫码预付款下单
     */
    Map<String, String> scan(String tradeNo, String subject, String body, String fee);


    /**
     * 预下单
     */
    Map<String, String> preOrder(TradeType tradeType, String tradeNo, String subject, String body, String fee);

    /**
     * 预下单
     */
    Map<String, String> preOrder(AliPayRequest payRequest);


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
