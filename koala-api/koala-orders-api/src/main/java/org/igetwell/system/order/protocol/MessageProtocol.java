package org.igetwell.system.order.protocol;

/**
 * 消息协议常量
 */
public enum MessageProtocol {

    /**
     * SECKILL_CHARGE_ORDER_TOPIC 秒杀下单消息协议
     */
    SECKILL_ORDER_TOPIC("SECKILL_CHARGE_ORDER_TOPIC", "GID_SNOWALKE_TEST", "GID_SNOWALKE_TEST", "秒杀下单消息协议"),
    ;
    /**
     * 消息主题
     */
    private String topic;
    /**
     * 生产者组
     */
    private String producerGroup;
    /**
     * 消费者组
     */
    private String consumerGroup;
    /**
     * 消息描述
     */
    private String desc;

    MessageProtocol(String topic, String producerGroup, String consumerGroup, String desc) {
        this.topic = topic;
        this.producerGroup = producerGroup;
        this.consumerGroup = consumerGroup;
        this.desc = desc;
    }

    public String getTopic() {
        return topic;
    }

    public String getProducerGroup() {
        return producerGroup;
    }

    public String getDesc() {
        return desc;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }
}
