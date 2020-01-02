package org.igetwell.system.order.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.dto.request.OrderPay;
import org.igetwell.system.order.entity.TradeOrder;
import java.math.BigDecimal;

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

    /**
     * 修改订单状态
     * @param id
     * @param status
     */
    void updateOrderStatus(Long id, Integer status);

    /**
     * 一码付款
     * @param fee
     * @return
     */
    ResponseEntity scan(BigDecimal fee);

    ResponseEntity trade(OrderPay orderPay);
}
