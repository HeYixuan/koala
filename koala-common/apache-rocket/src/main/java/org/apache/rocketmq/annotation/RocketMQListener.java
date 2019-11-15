package org.apache.rocketmq.annotation;

import java.lang.annotation.*;

/**
 * 改注解运用在消费端的方法上，用来处理同一topic中不同的tag类型的消息
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RocketMQListener {

    /**
     * 订阅的tag
     */
    String tag() default "*";

    /**
     * 请求方消息类型
     */
    Class<?> messageClass() default Object.class;
}
