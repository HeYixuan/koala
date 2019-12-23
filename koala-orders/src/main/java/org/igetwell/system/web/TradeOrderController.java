package org.igetwell.system.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.entity.TradeOrder;
import org.igetwell.system.order.service.ITradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradeOrderController {

    @Autowired
    private ITradeOrderService iTradeOrderService;

    /**
     * 保存支付订单
     */
    @PostMapping("/orders/tradeOrder/saveOrder")
    public ResponseEntity saveOrder(@RequestBody TradeOrder order){
        iTradeOrderService.insert(order);
        return ResponseEntity.ok();
    }
}
