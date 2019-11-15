package org.apache.rocketmq.common.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 平台订单查询返回实体
 */
@Getter
@Setter
public class QueryOrderResponse implements Serializable {

    private static final long serialVersionUID = 8752405981800372807L;

    private Integer id;

    /**
     * 代理商订单号
     */
    private String orderNo;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 商品id
     */
    private String prodId;

    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 交易金额
     */
    private BigDecimal chargeMoney;

    /**
     * 订单下单时间
     */
    private Date chargeTime;

    /**
     * 订单结束时间
     */
    private Date finishTime;

    /**
     * 订单状态，1 初始化 2 处理中 3 失败 0 成功
     */
    private Integer orderStatus;

    /**
     * 记录状态 0 正常 1 已删除
     */
    private Byte recordStatus;

    private Date createTime;

    private Date updateTime;
}
