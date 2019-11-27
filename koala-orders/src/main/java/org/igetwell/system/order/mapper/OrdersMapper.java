package org.igetwell.system.order.mapper;

import org.igetwell.system.order.entity.Orders;

public interface OrdersMapper {

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

    int insert(Orders order);

    int update(Orders order);

    int deleteById(Long id);
}