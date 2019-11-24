package org.igetwell.system.order.service;

import org.igetwell.system.order.entity.Orders;

public interface IOrderService {

    /**
     * 根据订单号查询
     * @param orderNo
     * @return
     */
    Orders getOrderNo(String orderNo);

    /**
     * 查询已支付订单
     * @param memberId
     * @param mobile
     * @param goodsId
     * @return
     */
    Orders getOrder(String memberId, Long mobile, Long goodsId);

    Orders getCache(String orderNo);

    void insert(Orders order);

    void update(Orders order);

    void deleteById(Long id);

    boolean createOrder(String orderNo, Long mobile, Long goodsId);


}
