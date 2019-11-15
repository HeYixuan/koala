package org.apache.rocketmq.common.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 平台查单请求实体
 */
@Getter
@Setter
public class QueryOrderRequest implements Serializable {

    private static final long serialVersionUID = 7143860158400568786L;

    /**用户下单手机号*/
    private String mobile;
    /**商品id*/
    private String prodId;

}
