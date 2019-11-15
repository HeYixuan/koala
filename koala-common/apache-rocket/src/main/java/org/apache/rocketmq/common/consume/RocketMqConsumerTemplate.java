package org.apache.rocketmq.common.consume;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@ConfigurationProperties(prefix = "rocket.consumer")
public abstract class RocketMqConsumerTemplate<M> implements MqConsumer<M> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqConsumerTemplate.class);

    private String consumerGroup;
    private String nameServer;
    private String topic;
    private Integer consumeMessageBatchMaxSize;

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getConsumeMessageBatchMaxSize() {
        return consumeMessageBatchMaxSize;
    }

    public void setConsumeMessageBatchMaxSize(Integer consumeMessageBatchMaxSize) {
        this.consumeMessageBatchMaxSize = consumeMessageBatchMaxSize;
    }

    private DefaultMQPushConsumer defaultMQPushConsumer;

    private AtomicBoolean started = new AtomicBoolean(false);

    @PostConstruct
    @Override
    public void start() throws MQClientException {
        if (started.get()) {
            throw new IllegalArgumentException("this consumer is already init");
        }
        if (StringUtils.isEmpty(this.consumerGroup)) {
            throw new IllegalArgumentException("please setting consumerGroup");
        }
        if (StringUtils.isEmpty(this.nameServer)) {
            throw new IllegalArgumentException("please setting nameServer");
        }

        if (this.defaultMQPushConsumer == null) {
            this.defaultMQPushConsumer = new DefaultMQPushConsumer();
        }
        this.defaultMQPushConsumer.setConsumerGroup(this.consumerGroup);
        this.defaultMQPushConsumer.setNamesrvAddr(this.nameServer);
        // 从消息队列头开始消费
        this.defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 集群消费模式
        this.defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        //设置一次消费消息的条数，默认为1条
        if (this.consumeMessageBatchMaxSize != null){
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(this.consumeMessageBatchMaxSize);
        }
        // 注册消息监听器
        this.defaultMQPushConsumer.registerMessageListener(this);

        String [] topic = getTopics();
        // 订阅主题
        this.defaultMQPushConsumer.subscribe(topic[0], topic[1]);

        this.defaultMQPushConsumer.start();
        LOGGER.info("consumer is start ! consumerGroup:{}, nameServer:{}", consumerGroup, nameServer);
        this.started.set(true);
    }

    @PreDestroy
    @Override
    public void shutdown() {
        if (started.get()) {
            this.defaultMQPushConsumer.shutdown();
            LOGGER.info("[consumer is stop !]");
            started.set(false);
        }
    }

    @Override
    public DefaultMQPushConsumer getConsumer() {
        return this.defaultMQPushConsumer;
    }

    private String[] getTopics() {
        String[] topics = this.topic.split(";");
        for (String topic : topics) {
            String[] topicTags = topic.split("~");
            return topicTags;
        }
        return null;
    }

    protected abstract void consume(String topic, String tag, String message);
}
