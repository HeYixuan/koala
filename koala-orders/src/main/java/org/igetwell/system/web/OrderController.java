package org.igetwell.system.web;

import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.RedisUtils;
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
    private RedisUtils redisUtils;

    @Autowired
    private Sequence sequence;

    @PostMapping("/orders/getNo")
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

    @PostMapping("/orders/lock")
    public void lock() {
        //加锁
        if (redisUtils.lock("testLock", "123456",30000,3)){

            try {
//                执行业务
                System.out.println("加锁成功，做业务");
                redisUtils.incr("inc");
                //Thread.sleep(3000);
                System.out.println("业务执行结束");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                //解锁
                redisUtils.unlock("testLock", "123456");
            }
        }
    }

    @PostMapping("/orders/unlock")
    public void unlock() {
        redisUtils.unlock("testLock2", "1234567");
    }

}
