package org.igetwell.system.member.consume;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//@Service
//@RocketMQMessageListener(topic = "topic1", consumerGroup = "pay-test")
public class MqConsumer{// implements RocketMQListener<String>{

    private static final Logger LOGGER = LoggerFactory.getLogger(MqConsumer.class);

    //@Override
//    public void onMessage(String message) {
//        LOGGER.error("received message: {}", message);
//    }



    /*@Slf4j
    @Service
    @RocketMQMessageListener(topic = "test-topic-2", consumerGroup = "my-consumer_test-topic-2")
    public class MyConsumer2 implements RocketMQListener<OrderPaidEvent>{
        public void onMessage(OrderPaidEvent orderPaidEvent) {
            LOGGER.info("received orderPaidEvent: {}", orderPaidEvent);
        }
    }*/
}
