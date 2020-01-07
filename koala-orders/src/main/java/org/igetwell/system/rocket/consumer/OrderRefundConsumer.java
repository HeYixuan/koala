package org.igetwell.system.rocket.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.igetwell.common.enums.OrderStatus;
import org.igetwell.common.enums.OrderType;
import org.igetwell.common.uitls.BigDecimalUtils;
import org.igetwell.common.uitls.CharacterUtils;
import org.igetwell.system.order.entity.RefundOrder;
import org.igetwell.system.order.entity.TradeOrder;
import org.igetwell.system.order.protocol.OrderRefundProtocol;
import org.igetwell.system.order.service.IRefundOrderService;
import org.igetwell.system.order.service.ITradeOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 支付订单退款成功消费者
 */
@Service
@RocketMQMessageListener(topic = "refund-order-success", consumerGroup = "refund-order-success", selectorExpression = "refund-order-success")
public class OrderRefundConsumer implements RocketMQListener<OrderRefundProtocol> {

    private static final Logger logger = LoggerFactory.getLogger(OrderTradeConsumer.class);

    @Autowired
    private ITradeOrderService iTradeOrderService;
    @Autowired
    private IRefundOrderService iRefundOrderService;


    @Override
    public void onMessage(OrderRefundProtocol protocol) {
        String outNo = protocol.getOutNo();
        String tradeNo = protocol.getTradeNo();
        String transactionId = protocol.getTransactionId();
        String timestamp = protocol.getTimestamp();
        String refundNo = protocol.getRefundId();
        String refundAccount = protocol.getRefundAccount();
        logger.info("[订单退款成功消费者]-商户退款单号={}, 商户订单号={}, 微信支付交易号={}, 支付金额={}, 支付时间={}.", outNo, tradeNo, transactionId, timestamp);

        RefundOrder orders = iRefundOrderService.get(transactionId, tradeNo, outNo);
        //如果订单不存在,或订单状态不是支付中,直接消费,不能直接修改状态为支付成功.
        if (orders == null && (orders.getStatus() != OrderStatus.CREATE.getValue() || orders.getStatus() != OrderStatus.REFUNDING.getValue())) {
            logger.info("[订单退款成功消费者]-根据商户退款单号：{}, 商户订单号：{}, 微信支付交易单号：{}. 未查询到待退款订单记录,消费成功", outNo, tradeNo, transactionId);
            return;
        }

        TradeOrder tradeOrder = iTradeOrderService.getOrder(tradeNo);
        if (orders == null && orders.getStatus() != OrderStatus.PAID.getValue()) {
            logger.info("[订单退款成功消费者]-根据商户订单号：{} 未查询到已支付订单记录, 无法进行处理回滚事物. 消费成功", outNo, tradeNo, transactionId);
            return;
        }

        RefundOrder order = new RefundOrder();
        order.setId(orders.getId());
        order.setStatus(OrderStatus.REFUND_SUCCESS.getValue());
        if (!CharacterUtils.isBlank(refundNo)) {
            order.setOutRefundNo(refundNo);
        }
        order.setRefundAccount(refundAccount);
        order.setRefundTime(timestamp);



        if (!StringUtils.isEmpty(tradeOrder)) {
            if (tradeOrder.getOrderType() == OrderType.CHARGE.getValue()) {
                //执行充值回滚
            }
            if (tradeOrder.getOrderType() == OrderType.CONSUME.getValue()) {
                //执行消费回滚
            }
            if (tradeOrder.getOrderType() == OrderType.OPEN.getValue()) {
                //执行开卡回滚
            }
        }
        iRefundOrderService.update(order);

        //如果退款金额等于支付金额,直接改支付单为已退款订单,否则根据商户交易单和微信交易流水号查询记录金额汇总
        List<RefundOrder> orderList = iRefundOrderService.get(transactionId, tradeNo);
        BigDecimal totalFee = BigDecimal.ZERO;
        for (int i = 0; i < orderList.size(); i++) {
            totalFee = BigDecimalUtils.add(totalFee, orderList.get(i).getRefundFee());
        }
        if (BigDecimalUtils.equals(totalFee, tradeOrder.getFee())) {
            iTradeOrderService.updateStatus(tradeNo, OrderStatus.REFUND.getValue());
        }
    }
}
