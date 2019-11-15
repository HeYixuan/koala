package org.apache.rocketmq.common.consume.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 消息监听
 */
public interface MessageListener {

    /**
     * 消费消息
     * @param msg
     * @param context
     * @return
     */
    ConsumeConcurrentlyStatus consume(final MessageExt msg, final ConsumeConcurrentlyContext context);

    /**
     * 如果消费端的isOrdered配置为true,此方法必须实现逻辑
     * 消费顺序消息
     * @param msg
     * @param context
     * @return
     */
    ConsumeOrderlyStatus consumeOrdered(final MessageExt msg, final ConsumeConcurrentlyContext context);
}
