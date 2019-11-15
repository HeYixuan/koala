package org.igetwell.system.member.service;


import org.igetwell.order.dto.OrderDto;
import org.igetwell.order.entity.SeckillOrder;

public interface ISecKillChargeService {

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

    /**
     * 秒杀订单入库
     * @param mobile 手机号
     * @param orderNo 订单号
     * @param productId 商品ID
     * @return
     */
    boolean chargeOrder(String mobile, String orderNo, String productId);

}
