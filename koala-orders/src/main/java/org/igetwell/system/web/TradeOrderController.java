package org.igetwell.system.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.dto.request.OrderPay;
import org.igetwell.system.order.entity.TradeOrder;
import org.igetwell.system.order.service.ITradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class TradeOrderController {

    @Autowired
    private ITradeOrderService iTradeOrderService;

    @PostMapping("/orders/get/{id}")
    public ResponseEntity get(@PathVariable("id") Long id){
        TradeOrder order = iTradeOrderService.get(id);
        return ResponseEntity.ok(order);
    }

    /**
     * 订单创建
     * @param orderPay
     * @return
     */
    @PostMapping("/orders/trade")
    public ResponseEntity trade(@RequestBody OrderPay orderPay){
        return iTradeOrderService.trade(orderPay);
    }

    /**
     * 订单创建
     */
    @GetMapping("/orders/trade/scan")
    public ResponseEntity trade(@RequestParam(value="money", defaultValue="0.01") BigDecimal money){
        return iTradeOrderService.scan(money);
    }


    /**
     * 保存支付订单
     */
    @PostMapping("/orders/tradeOrder/saveOrder")
    public ResponseEntity saveOrder(@RequestBody TradeOrder order){
        iTradeOrderService.insert(order);
        return ResponseEntity.ok();
    }
}
