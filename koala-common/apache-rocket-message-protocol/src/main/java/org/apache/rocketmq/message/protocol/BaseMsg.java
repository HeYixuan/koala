package org.apache.rocketmq.message.protocol;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础协议类
 */
@Slf4j
public abstract class BaseMsg {


    /**版本号，默认1.0*/
    private String version = "1.0";
    /**主题名*/
    private String topicName;

    public abstract String encode();

    public abstract void decode(String msg);

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

}


