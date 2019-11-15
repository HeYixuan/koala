package org.apache.rocketmq.common.produce;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
@ConfigurationProperties(prefix = "rocket.producer")
public class RocketMqProducerTemplate<M> implements MqProducer<M> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqProducerTemplate.class);

    private String producerGroup;

    private String nameServer;

    private Integer timeOut;

    private Integer retryTimesWhenSendFailed;

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public Integer getRetryTimesWhenSendFailed() {
        return retryTimesWhenSendFailed;
    }

    public void setRetryTimesWhenSendFailed(Integer retryTimesWhenSendFailed) {
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
    }

    private DefaultMQProducer defaultMQProducer;

    private AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public void start() throws MQClientException {
        if (started.get()) {
            throw new IllegalArgumentException("this producer is already init");
        }
        if (StringUtils.isEmpty(this.producerGroup)) {
            throw new IllegalArgumentException("please setting producerGroup");
        }
        if (StringUtils.isEmpty(this.nameServer)) {
            throw new IllegalArgumentException("please setting nameServer");
        }

        if (this.defaultMQProducer == null) {
            this.defaultMQProducer = new DefaultMQProducer();
        }
        this.defaultMQProducer.setProducerGroup(this.producerGroup);
        this.defaultMQProducer.setNamesrvAddr(this.nameServer);
        if(this.timeOut != null){
            defaultMQProducer.setSendMsgTimeout(this.timeOut);
        }
        if(this.retryTimesWhenSendFailed != null){
            defaultMQProducer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
        }
        this.defaultMQProducer.start();
        LOGGER.info("producer is start ! producerGroup:{}, nameServer:{}", producerGroup, nameServer);
        this.started.set(true);
    }

    @Override
    public void shutdown() {
        if (started.get()) {
            this.defaultMQProducer.shutdown();
            started.set(false);
        }
    }

    @Override
    public DefaultMQProducer getProducer(){
        return this.defaultMQProducer;
    }

}
