package org.igetwell.system.order.mapper;

import org.igetwell.system.order.entity.Orders;

public interface OrdersMapper {

    Orders getOrderNo(String orderNo);

    int insert(Orders order);

    int update(Orders order);

    int deleteById(Long id);
}