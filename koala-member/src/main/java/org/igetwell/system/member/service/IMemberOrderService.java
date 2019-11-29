package org.igetwell.system.member.service;


import org.igetwell.system.order.entity.Orders;

public interface IMemberOrderService {

    /**
     * 查询会员所有订单
     * @param memberId
     * @return
     */
    Orders getMemberOrder(Long memberId);
}
