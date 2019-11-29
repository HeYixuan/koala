package org.igetwell.system.member.service.impl;


import lombok.AllArgsConstructor;
import org.igetwell.system.member.service.IMemberOrderService;
import org.igetwell.system.order.entity.Orders;
import org.igetwell.system.order.feign.OrdersClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MemberOrderService implements IMemberOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberOrderService.class);

    @Autowired
    private OrdersClient ordersClient;

    /**
     * 查询会员所有订单
     * @param memberId
     * @return
     */
    public Orders getMemberOrder(Long memberId) {
        return ordersClient.getMemberOrder(memberId);
    }

}
