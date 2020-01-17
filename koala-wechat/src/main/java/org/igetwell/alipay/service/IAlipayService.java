package org.igetwell.alipay.service;

import org.igetwell.common.enums.TradeType;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.bean.dto.request.AliPayRequest;
import org.igetwell.system.bean.dto.request.AliRefundRequest;

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
     * 创建订单 JSAPI 通过支付宝交易号唤起收银台支付
     */
    Map<String, String> jsapi(String tradeNo, String subject, String body, String fee);

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
    void refund(String transactionId, String tradeNo, String outNo, String fee);

    /**
     * 支付宝退款
     */
    void refund(AliRefundRequest refundRequest);

    /**
     * 处理支付宝支付回调
     * @return
     */
    String payNotify(HttpServletRequest request);

    /**
     * 查询支付订单
     * @param transactionId 支付宝订单号
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

    /**
     * 处理支付宝服务器同步通知
     * @return
     */
    String syncNotify(HttpServletRequest request);
}
