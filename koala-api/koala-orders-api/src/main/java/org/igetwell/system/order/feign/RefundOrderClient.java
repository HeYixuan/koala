package org.igetwell.system.order.feign;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.dto.request.RefundTradeRequest;
import org.igetwell.system.order.dto.request.RefundTransactionRequest;
import org.igetwell.system.order.entity.RefundOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "koala-orders")
public interface RefundOrderClient {

    /**
     * 根据微信支付单号和商户订单号查询已退款订单
     */
    @PostMapping("/orders/refundOrder/tradeOrder")
    List<RefundOrder> getOrder(@RequestBody RefundTradeRequest request);

    /**
     * 综合查询退款订单
     */
    @PostMapping("/orders/refundOrder/getOrder")
    RefundOrder getOrder(@RequestBody RefundTransactionRequest request);

    /**
     * 修改退款订单
     */
    @PostMapping("/orders/refundOrder/updateOrder")
    ResponseEntity update(@RequestBody RefundOrder refundOrder);
}
