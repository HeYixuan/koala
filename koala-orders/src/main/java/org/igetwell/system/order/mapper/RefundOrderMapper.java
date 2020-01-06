package org.igetwell.system.order.mapper;

import org.igetwell.system.order.entity.RefundOrder;

public interface RefundOrderMapper {

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    RefundOrder get(Long id);

    int deleteById(Long id);

    int insert(RefundOrder refundOrder);

    int update(RefundOrder refundOrder);

    /**
     * 根据微信支付单号和商户订单号查询
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @return
     */
    RefundOrder getTrade(String transactionId, String tradeNo);

    /**
     * 根据商户退款单号、微信支付单号和商户订单号查询
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @param outNo 商户退款单号
     * @return
     */
    RefundOrder getOrder(String transactionId, String tradeNo, String outNo);

    /**
     * 根据微信支付单号、商户订单号、微信退款单号、商户退款单号查询
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @param outRefundNo 微信退款单号
     * @param outNo 商户退款单号
     * @return
     */
    RefundOrder getTransaction(String transactionId, String tradeNo, String outRefundNo, String outNo);

    /**
     * 根据微信支付单号和商户订单号修改为状态
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @return
     */
    int updateRefunding(String transactionId, String tradeNo, Integer status);
}