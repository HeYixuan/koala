package org.igetwell.system.order.mapper;


import org.igetwell.system.order.entity.TradeOrder;

public interface TradeOrderMapper {

    TradeOrder get(Long id);

    TradeOrder getOrder(String orderNo);

    void deleteById(Long id);

    int insert(TradeOrder order);

    int update(TradeOrder order);

    /**
     * 根据订单ID修改支付订单状态
     */
    int updateOrderStatus(Long id, Integer status);

    /**
     * 根据商户交易号修改支付订单状态
     */
    int updateStatus(String tradeNo, Integer status);
}