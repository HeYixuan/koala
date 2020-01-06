package org.igetwell.system.rocket.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.igetwell.common.enums.OrderStatus;
import org.igetwell.common.enums.OrderType;
import org.igetwell.system.order.entity.TradeOrder;
import org.igetwell.system.order.protocol.OrderPayProtocol;
import org.igetwell.system.order.service.ITradeOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 支付订单支付成功消费者
 */
@Service
@RocketMQMessageListener(topic = "trade-order-success", consumerGroup = "trade-order-success", selectorExpression = "trade-order-success")
public class OrderTradeConsumer implements RocketMQListener<OrderPayProtocol> {

    private static final Logger logger = LoggerFactory.getLogger(OrderTradeConsumer.class);

    @Autowired
    private ITradeOrderService iTradeOrderService;

    @Override
    public void onMessage(OrderPayProtocol protocol) {
        String tradeNo = protocol.getTradeNo();
        String transactionId = protocol.getTransactionId();
        BigDecimal totalFee = protocol.getTotalFee();
        String timestamp = protocol.getTimestamp();
        logger.info("[订单支付成功消费者]-商户订单号={}, 交易号={}, 支付金额={}, 支付时间={}.", tradeNo, transactionId, totalFee, timestamp);
        TradeOrder orders = iTradeOrderService.getOrder(tradeNo);
        //如果订单不存在,或订单状态不是支付中,直接消费,不能直接修改状态为支付成功.
        if (orders == null && orders.getStatus() != OrderStatus.PENDING.getValue()) {
            logger.info("[订单支付成功消费者]-根据商户订单号：{} 未查询到待支付订单记录,消费成功", tradeNo);
            return;
        }
        TradeOrder order = new TradeOrder();
        order.setId(orders.getId());
        order.setStatus(OrderStatus.PAID.getValue());
        order.setTransactionId(transactionId);
        order.setSuccessTime(timestamp);
        iTradeOrderService.update(order);
        if (orders.getOrderType() == OrderType.CHARGE.getValue()) {
            //执行充值
        }
        if (orders.getOrderType() == OrderType.CONSUME.getValue()) {
            //执行消费
        }
        if (orders.getOrderType() == OrderType.OPEN.getValue()) {
            //执行开卡
        }

    }
}
