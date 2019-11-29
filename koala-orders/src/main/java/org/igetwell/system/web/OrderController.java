package org.igetwell.system.web;

import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.entity.Orders;
import org.igetwell.system.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private Sequence sequence;

    @PostMapping("/getNo")
    public String getNextNo(){
        System.err.println(sequence.nextValue());
        System.err.println(sequence.nextNo());
        return sequence.nextNo();
    }

    /**
     * 查询会员所有订单
     * @param memberId
     * @return
     */
    @PostMapping("/orders/getMemberOrder/{memberId}")
    public Orders getMemberOrder(@PathVariable("memberId") Long memberId) {
        Orders orders = iOrderService.getMemberOrder(memberId);
        return orders;
    }
}
