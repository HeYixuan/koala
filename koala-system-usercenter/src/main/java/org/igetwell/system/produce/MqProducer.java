package org.igetwell.system.produce;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.system.member.MessageProtocol;
import org.igetwell.system.member.SkillOrderMsgProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Component
public class MqProducer{

    private static final Logger LOGGER = LoggerFactory.getLogger(MqProducer.class);

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void send() {
        LOGGER.info("producer is start!");
        SkillOrderMsgProtocol protocol = new SkillOrderMsgProtocol("No:20190101", "15218725510", "115554886677544", new BigDecimal(88.01));

        //rocketMQTemplate.send("topic1", MessageBuilder.withPayload("Hello, World! I'm from spring message").build());
        rocketMQTemplate.convertAndSend(MessageProtocol.SECKILL_ORDER_TOPIC.getTopic(), protocol);
    }

}
