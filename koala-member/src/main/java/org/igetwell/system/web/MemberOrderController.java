package org.igetwell.system.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.member.service.IMemberOrderService;
import org.igetwell.system.order.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberOrderController {

    @Autowired
    private IMemberOrderService iMemberOrderService;

    @PostMapping("/member/getMemberOrder/{memberId}")
    public ResponseEntity getMemberOrder(@PathVariable("memberId") Long memberId) {
        Orders orders = iMemberOrderService.getMemberOrder(memberId);
        return ResponseEntity.ok(orders);
    }
}
