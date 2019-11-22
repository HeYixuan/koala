package org.igetwell.system.order.service;

import org.igetwell.system.order.entity.Orders;

public interface IOrderService {

    Orders getOrderNo(String orderNo);

    void insert(Orders order);

    void update(Orders order);

    void deleteById(Long id);
}
