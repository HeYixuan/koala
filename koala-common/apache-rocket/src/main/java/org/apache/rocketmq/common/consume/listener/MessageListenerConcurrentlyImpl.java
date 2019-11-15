package org.apache.rocketmq.common.consume.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.consume.RocketMqConsumerTemplate;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 并发消费监听默认实现
 */
@Component
public class MessageListenerConcurrentlyImpl extends RocketMqConsumerTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListenerConcurrentlyImpl.class);

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        try {
            for (MessageExt msg : msgs) {
                String topic = msg.getTopic();
                String message = new String(msg.getBody(), "UTF-8");
                // 标签
                String tag = msg.getTags();
                String keys = msg.getKeys();
                //LOGGER.error(String.format("根据Topic：%s和Tag:%s 没有找到对应的处理消息的服务",topic,tag));
                LOGGER.info("根据Topic：{} 和Tag:{} 路由到的服务为: 开始调用处理消息", topic, tag);
                // 消息解码
                int reconsumeTimes = msg.getReconsumeTimes();
                String msgId = msg.getMsgId();
                String logSuffix = ",msgId=" + msgId + ",reconsumeTimes=" + reconsumeTimes;
                consume(topic, tag, message);
                LOGGER.info("[消费成功]--接收到消息, message={}, {}", message, logSuffix);
            }
        } catch (Exception e){
            LOGGER.error("[消费失败]--正在重试, message={}, {}");
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    protected void consume(String topic, String tag, String message){
        if ("topic1".equals(topic)) {
            System.err.println("下订单:" + message);

        } else if ("topic2".equals(topic)) {
            System.err.println("支付订单" + message);
        }
    }
}
