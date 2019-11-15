package org.apache.rocketmq.common.produce;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;


public interface MqProducer<M> {

    void start() throws MQClientException;

    void shutdown();

    DefaultMQProducer getProducer();

}
