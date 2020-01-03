package org.igetwell.system.order.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.dto.request.OrderRefundPay;
import org.igetwell.system.order.dto.request.RefundTradeRequest;
import org.igetwell.system.order.dto.request.RefundTransactionRequest;
import org.igetwell.system.order.entity.RefundOrder;
import org.igetwell.system.order.dto.request.RefundOrderRequest;

public interface IRefundOrderService {

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    RefundOrder get(Long id);

    /**
     * 根据微信支付单号和商户订单号查询
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @return
     */
    RefundOrder get(String transactionId, String tradeNo);

    /**
     * 根据微信支付单号、商户订单号、微信退款单号、商户退款单号查询
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @param outRefundNo 微信退款单号
     * @param outNo 商户退款单号
     * @return
     */
    RefundOrder get(String transactionId, String tradeNo, String outRefundNo, String outNo);

    void deleteById(Long id);

    void insert(RefundOrder refundOrder);

    void update(RefundOrder refundOrder);

    /**
     * 保存退款记录
     * @param request
     */
    void insert(RefundOrderRequest request);

    /**
     * 用户发起退款
     * @param request
     */
    ResponseEntity refundOrder(RefundOrderRequest request);

    /**
     * 退款订单入队
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @return
     */
    ResponseEntity refundOrderQueue(String transactionId, String tradeNo);

    /**
     * 根据微信支付单号和商户订单号查询
     * @return
     */
    RefundOrder getOrder(RefundTradeRequest request);

    /**
     * 根据微信支付单号、商户订单号、微信退款单号、商户退款单号查询
     * @return
     */
    RefundOrder getOrder(RefundTransactionRequest request);

    /**
     * 用户发起退款
     */
    ResponseEntity refund(OrderRefundPay refundPay);

}
