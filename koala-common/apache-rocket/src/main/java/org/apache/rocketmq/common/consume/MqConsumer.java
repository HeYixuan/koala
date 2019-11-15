package org.apache.rocketmq.common.consume;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;

public interface MqConsumer<M> extends MessageListenerConcurrently {

    void start() throws MQClientException;

    void shutdown();

    DefaultMQPushConsumer getConsumer();
}
