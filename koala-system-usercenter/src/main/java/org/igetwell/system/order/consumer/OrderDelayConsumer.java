package org.igetwell.system.order.consumer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.system.order.entity.Orders;
import org.igetwell.system.order.protocol.OrderProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * 订单延迟消息队列
 */
@Service
@RocketMQMessageListener(topic = "order-check-timout", consumerGroup = "order-check-timout", selectorExpression = "order-check-timout")
public class OrderDelayConsumer implements RocketMQListener<OrderProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDelayConsumer.class);

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessage(OrderProtocol protocol) {
        String orderNo = protocol.getOrderNo();
        try {
            LOGGER.info("[秒杀订单异步延迟消息消费者]-订单号={}, 商品ID={}, 手机号={}, 金额={}.", orderNo, protocol.getGoodsId(), protocol.getMobile(), protocol.getMoney().doubleValue());
            Orders order = redisUtils.get(orderNo);
            //如果日志记录存在并且是待支付,说明订单超时
            if (order != null && order.getPayStatus().intValue() == 0){
                LOGGER.info("[秒杀订单异步延迟消息消费者]-订单超时, 开始投递超时订单记录消息, 订单号={}.", orderNo);
                rocketMQTemplate.getProducer().setProducerGroup("order-timeout");
                rocketMQTemplate.asyncSend("order-timeout:order-timeout", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                    @Override
                    public void onSuccess(SendResult var) {
                        LOGGER.info("[秒杀订单异步延迟消息消费者]-订单超时, 投递超时订单记录成功, 投递结果：{}.", var);
                    }
                    @Override
                    public void onException(Throwable var) {
                        LOGGER.info("[秒杀订单异步延迟消息消费者]-订单超时, 投递超时订单记录失败, 异常信息：{}.", var);
                    }
                });
            }
        } catch (Exception e){
            int retryTimes = rocketMQTemplate.getProducer().getRetryTimesWhenSendAsyncFailed();
            LOGGER.error("[秒杀订单异步延迟消息消费者]-超时订单投递异常, 正在重试第{}次, 订单号={}.", retryTimes, orderNo, e);
        }
    }
}
