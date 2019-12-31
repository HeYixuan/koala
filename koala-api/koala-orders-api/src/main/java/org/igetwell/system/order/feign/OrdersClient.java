package org.igetwell.system.order.feign;


import org.igetwell.system.order.entity.Orders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "koala-orders")
public interface OrdersClient {

    /**
     * 查询会员所有订单
     *
     * @param memberId
     * @return
     */
    @PostMapping("/orders/getMemberOrder/{memberId}")
    Orders getMemberOrder(@PathVariable("memberId") Long memberId);
}
