package org.igetwell.system.member.consume;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.order.entity.SeckillOrder;
import org.igetwell.system.member.SkillOrderMsgProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * 秒杀消费者 方案2
 */
@Service
@RocketMQMessageListener(topic = "order-check-timout", consumerGroup = "order-check-timout", selectorExpression = "order-check-timout")
public class OrderDelayConsumer implements RocketMQListener<SkillOrderMsgProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDelayConsumer.class);
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessage(SkillOrderMsgProtocol protocol) {
        String orderNo = protocol.getOrderNo();
        try {
            LOGGER.info("[秒杀订单异步延迟消息消费者：订单号={}, 商品ID={}, 手机号={}, 金额={}]", protocol.getOrderNo(), protocol.getProdId(),
                    protocol.getMobile(), protocol.getMoney().doubleValue());
            SeckillOrder order = (SeckillOrder) redisUtils.getObject(orderNo);
            //如果日志记录存在，并且是待支付
            if (order != null && order.getOrderStatus().intValue() == 1){
                LOGGER.info("[秒杀订单异步延迟消息消费者]：订单超时, 开始投递超时订单记录消息, 订单号={}", orderNo);

                rocketMQTemplate.getProducer().setProducerGroup("order-timeout");
                rocketMQTemplate.asyncSend("order-timeout:order-timeout", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        LOGGER.info("[秒杀订单异步延迟消息消费者]：订单超时, 投递超时订单记录成功, 投递结果={}", sendResult);
                    }
                    @Override
                    public void onException(Throwable throwable) {
                        LOGGER.info("[秒杀订单异步延迟消息消费者]：订单超时, 投递超时订单记录失败, 异常信息{}", throwable);
                    }
                });
            }
        } catch (Exception e){
            int retryTimes = rocketMQTemplate.getProducer().getRetryTimesWhenSendAsyncFailed();
            LOGGER.info("[秒杀订单异步延迟消息消费者]：超时订单投递异常, 正在重试第{}次, 订单号={}", retryTimes, orderNo, e);
        }

    }
}
