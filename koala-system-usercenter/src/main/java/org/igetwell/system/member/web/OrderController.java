package org.igetwell.system.member.web;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.order.entity.SeckillProduct;
import org.igetwell.system.member.dto.request.ChargeOrderRequest;
import org.igetwell.system.member.service.IMemberOrderService;
import org.igetwell.system.member.service.ISecKillProductService;
import org.igetwell.system.produce.MqProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;


@RestController
public class OrderController {

    @Autowired
    private MqProducer producer;

    @Autowired
    private IMemberOrderService iMemberOrderService;

    @Autowired
    private ISecKillProductService secKillProductService;

    /**
     * topic,消息依赖于topic
     */
    private static final String topic = "topic1";


    @RequestMapping("/api/test")
    public ResponseEntity callback(String text) throws Exception {

        producer.send();

        return ResponseEntity.ok();
    }

    /**
     * 查询订单
     * @return
     */
    @PostMapping("/api/order/getOrder")
    public ResponseEntity getOrder(){

        return ResponseEntity.ok();
    }

    /**
     * 用户下单
     *  1.检查
     *  2.发送MQ消息
     * @return
     */
    @PostMapping("/api/order/placeOrder")
    public ResponseEntity placeOrder(@RequestBody ChargeOrderRequest chargeOrderRequest){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String sessionId = attributes.getSessionId();
        ResponseEntity responseEntity = iMemberOrderService.placeOrder(chargeOrderRequest);
        return responseEntity;
    }


    @PostMapping("/api/getProductList")
    public ResponseEntity getList(){
        List<SeckillProduct> products = secKillProductService.getList();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/api/getProduct/{prodId}")
    public ResponseEntity getProduct(@PathVariable("prodId") String prodId){
        SeckillProduct product = secKillProductService.get(prodId);
        return ResponseEntity.ok(product);
    }


}
