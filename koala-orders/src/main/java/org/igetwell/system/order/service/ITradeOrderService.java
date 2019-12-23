package org.igetwell.system.order.service;

import org.igetwell.system.order.entity.TradeOrder;

public interface ITradeOrderService {

    TradeOrder get(Long id);

    /**
     * 根据商户订单号获取缓存订单数据
     * @param orderNo
     * @return
     */
    TradeOrder getCache(String orderNo);

    void deleteById(Long id);

    void insert(TradeOrder order);

    void update(TradeOrder order);
}
