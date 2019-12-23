package org.igetwell.system.order.feign;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.entity.TradeOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "koala-orders")
public interface TradeOrderClient {

    /**
     * 保存支付订单
     */
    @PostMapping("/orders/tradeOrder/saveOrder")
    ResponseEntity saveOrder(@RequestBody TradeOrder order);
}
