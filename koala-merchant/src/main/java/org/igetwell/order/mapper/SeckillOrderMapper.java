package org.igetwell.order.mapper;

import org.igetwell.order.dto.OrderDto;
import org.igetwell.order.entity.SeckillOrder;


public interface SeckillOrderMapper {

    /**
     * 查询订单信息
     */
    SeckillOrder selectByOrderNo(String orderNo);

    /**
     * 查询订单信息
     */
    SeckillOrder getOrder(OrderDto dto);

    int delete(Integer id);

    int insert(SeckillOrder order);

    int update(SeckillOrder order);
}