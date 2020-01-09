package org.igetwell.system.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.dto.request.OrderRefundPay;
import org.igetwell.system.order.dto.request.RefundOrderRequest;
import org.igetwell.system.order.dto.request.RefundTradeRequest;
import org.igetwell.system.order.dto.request.RefundTransactionRequest;
import org.igetwell.system.order.entity.RefundOrder;
import org.igetwell.system.order.service.IRefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class RefundOrderController {

    @Autowired
    private IRefundOrderService iRefundOrderService;

    /**
     * 用户申请退款
     */
    @PostMapping("/orders/validateSign")
    public ResponseEntity validateSign(@RequestParam(value = "appId") String appId, @RequestParam(value = "timestamp") String timestamp, @RequestParam(value = "mchId") String mchId, @RequestParam(value="money", defaultValue="0.01") BigDecimal money) {
        return ResponseEntity.ok();
    }

    /**
     * 用户申请退款
     */
    @PostMapping("/orders/validateSign2")
    public ResponseEntity validateSign2(@RequestBody(required=false) OrderRefundPay refundPay) {
        return ResponseEntity.ok();
    }

    /**
     * 用户申请退款
     */
    @PostMapping("/orders/refundOrder")
    public ResponseEntity refundOrder(@RequestBody RefundOrderRequest request) {
        return iRefundOrderService.refundOrder(request);
    }

    /**
     * 根据微信支付单号和商户订单号查询已退款订单
     */
    @PostMapping("/orders/refundOrder/tradeOrder")
    public List<RefundOrder> getOrder(@RequestBody RefundTradeRequest request){
        return iRefundOrderService.getOrder(request);
    }

    /**
     * 综合查询退款订单
     */
    @PostMapping("/orders/refundOrder/getOrder")
    public RefundOrder getOrder(@RequestBody RefundTransactionRequest request) {
        return iRefundOrderService.getOrder(request);
    }

    /**
     * 修改退款订单
     */
    @PostMapping("/orders/refundOrder/updateOrder")
    public ResponseEntity update(@RequestBody RefundOrder refundOrder){
        iRefundOrderService.update(refundOrder);
        return ResponseEntity.ok();
    }


    /**
     * 用户申请退款
     */
    @PostMapping("/orders/refundOrder/refund")
    public ResponseEntity refund(@RequestBody OrderRefundPay refundPay) {
        return iRefundOrderService.refund(refundPay);
    }
}
