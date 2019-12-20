package org.igetwell.rocket.consume;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.igetwell.system.order.dto.request.RefundTradeRequest;
import org.igetwell.system.order.entity.RefundOrder;
import org.igetwell.system.order.feign.RefundOrderClient;
import org.igetwell.system.order.protocol.RefundPayCallProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 微信退款回调消费者
 */
@Service
@RocketMQMessageListener(topic = "refund-pay-order-call", consumerGroup = "refund-pay-order-call", selectorExpression = "refund-pay-order-call")
public class RefundOrderCallConsumer implements RocketMQListener<RefundPayCallProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefundOrderConsumer.class);

    @Autowired
    private RefundOrderClient refundOrderClient;

    @Override
    public void onMessage(RefundPayCallProtocol protocol) {
        String tradeNo = protocol.getTradeNo();
        String transactionId = protocol.getTransactionId();
        String outNo = protocol.getOutNo();
        String outRefundNo = protocol.getOutRefundNo();
        String status = protocol.getStatus();
        String refundTime = protocol.getRefundTime();
        String refundAccount = protocol.getRefundAccount();
        try {
            RefundTradeRequest request = new RefundTradeRequest(transactionId, tradeNo);
            RefundOrder order = refundOrderClient.getOrder(request);
            //如果退款订单不存在或者不是退款中拒绝,不予退款
            if (StringUtils.isEmpty(order) || order.getStatus() != 1) {
                LOGGER.info("[微信支付]-退款订单消费者. 微信支付单号：{}, 商户订单号：{}.未查询到退款记录.无法进行退款.消费成功.", transactionId, tradeNo);
                return;
            }
            LOGGER.info("[微信支付]-退款订单消费者微信退款回调开始 微信支付单号：{}, 商户订单号：{}.", transactionId, tradeNo);
            RefundOrder refundOrder = new RefundOrder();
            refundOrder.setId(order.getId());
            refundOrder.setTransactionId(transactionId);
            refundOrder.setOutNo(outNo);
            refundOrder.setTradeNo(tradeNo);
            refundOrder.setOutRefundNo(outRefundNo);
            refundOrder.setRefundAccount(refundAccount);
            //退款状态：SUCCESS-退款成功CHANGE-退款异常REFUNDCLOSE—退款关闭
            //退款状态：0-用户发起退款 1-退款中 2-退款成功 3-退款失败 4-退款关闭 5商户拒绝退款
            if (status.equalsIgnoreCase("SUCCESS")) {
                refundOrder.setStatus(2);
            }
            if (status.equalsIgnoreCase("CHANGE")) {
                refundOrder.setStatus(3);
            }
            if (status.equalsIgnoreCase("REFUNDCLOSE")) {
                refundOrder.setStatus(4);
            }
            refundOrder.setRefundTime(refundTime);
            refundOrderClient.update(refundOrder);//退款成功
            LOGGER.info("[微信支付]-退款订单消费者微信退款回调结束 微信支付单号：{}, 商户订单号：{}.", transactionId, tradeNo);
        } catch (Exception e) {
            LOGGER.error("[微信支付]-退款订单消费者微信退款回调异常.正在重试. 微信支付单号：{}, 商户订单号：{}.", transactionId, tradeNo, e);
            String message = String.format("[订单超时消费者]-退款订单消费者调用微信退款异常.正在重试. 微信支付单号：%s, 商户订单号：%s.", transactionId, tradeNo);
            throw new RuntimeException(message, e);
        }
    }
}
