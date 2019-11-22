package org.igetwell.system.member.consume;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.order.entity.SeckillOrder;
import org.igetwell.system.member.SkillOrderMsgProtocol;
import org.igetwell.system.member.service.ISecKillChargeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "order-return-pay", consumerGroup = "order-return-pay", selectorExpression = "order-return-pay")
public class OrderReturnPayConsumer implements RocketMQListener<SkillOrderMsgProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderReturnPayConsumer.class);

    @Autowired
    private ISecKillChargeService iSecKillChargeService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessage(SkillOrderMsgProtocol protocol) {
        try {
            LOGGER.info("[退款订单消费者：订单号={}, 商品ID={}, 手机号={}, 金额={}]", protocol.getOrderNo(), protocol.getProdId(),
                    protocol.getMobile(), protocol.getMoney().doubleValue());

            String orderNo = protocol.getOrderNo();
            SeckillOrder order = iSecKillChargeService.selectByOrderNo(orderNo);
            if (order == null){
                LOGGER.info("[退款订单消费者]-退款订单开始入库, 订单号={}.", orderNo);
                order = (SeckillOrder) redisUtils.getObject(orderNo);
                order.setOrderStatus(5);
                iSecKillChargeService.insert(order);
                LOGGER.info("[退款订单消费者]-退款订单入库成功, 订单号={}.", orderNo);
            }

            if (order != null && order.getOrderStatus() == 2){
                LOGGER.info("[退款订单消费者]-退款订单开始入库, 订单号={}.", orderNo);
                order.setOrderStatus(5);
                iSecKillChargeService.updateOrder(order);
                LOGGER.info("[退款订单消费者]-退款订单开始入库, 订单号={}.", orderNo);
            }
        } catch (Exception e){

        }
    }
}
