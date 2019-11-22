package org.igetwell.system.member.consume;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.order.entity.SeckillOrder;
import org.igetwell.system.member.SkillOrderMsgProtocol;
import org.igetwell.system.member.service.ISecKillChargeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "order-timeout", consumerGroup = "order-timeout", selectorExpression = "order-timeout")
public class OrderTimeoutConsumer implements RocketMQListener<SkillOrderMsgProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderTimeoutConsumer.class);

    @Autowired
    private ISecKillChargeService iSecKillChargeService;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void onMessage(SkillOrderMsgProtocol protocol) {
        try {
            LOGGER.info("[订单超时消费者：订单号={}, 商品ID={}, 手机号={}, 金额={}]", protocol.getOrderNo(), protocol.getProdId(),
                    protocol.getMobile(), protocol.getMoney().doubleValue());
            String orderNo = protocol.getOrderNo();
            String prodId= protocol.getProdId();
            String mobile= protocol.getMobile();

            SeckillOrder order = iSecKillChargeService.selectByOrderNo(orderNo);
            if (order != null){
                LOGGER.info("[订单超时消费者]-当前订单已入库,不需要重复消费!,orderNo={}.", orderNo);
                return;
            }
            order = (SeckillOrder) redisUtils.getObject(orderNo);
            if (order != null){
                order.setOrderStatus(2);
                int stock = (int) redisUtils.getObject("stock_"+ prodId);
                stock = stock + 1;
                LOGGER.info("[订单超时消费者]-超时订单开始入库, 回滚库存!, 订单号={}. 当前剩余库存={}", orderNo, stock);
                redisUtils.set("stock_"+prodId, stock);
                redisUtils.set(orderNo, order);
                //iSecKillChargeService.insert(order);
                LOGGER.info("[订单超时消费者]-超时订单入库成功, 消息消费成功!, 订单号：{}.", orderNo);
            }
        } catch (Exception e){
            LOGGER.error("[订单超时消费者]-超时订单入库失败，正在重试, 订单号：{}", protocol.getOrderNo());
            String message = String.format("[秒杀订单消费者]-秒杀订单入库失败，正在重试, 订单号：{}", protocol.getOrderNo());
            throw new RuntimeException(message);
        }
    }
}
