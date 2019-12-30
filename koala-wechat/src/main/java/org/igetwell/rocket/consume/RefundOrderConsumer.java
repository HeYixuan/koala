package org.igetwell.rocket.consume;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.igetwell.system.order.dto.request.RefundTradeRequest;
import org.igetwell.system.order.entity.RefundOrder;
import org.igetwell.system.order.feign.RefundOrderClient;
import org.igetwell.system.order.protocol.RefundPayProtocol;
import org.igetwell.wechat.app.service.IWxReturnPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 微信退款消费者
 */
@Service
@RocketMQMessageListener(topic = "refund-pay-order", consumerGroup = "refund-pay-order", selectorExpression = "refund-pay-order")
public class RefundOrderConsumer implements RocketMQListener<RefundPayProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefundOrderConsumer.class);

    @Autowired
    private RefundOrderClient refundOrderClient;

    @Autowired
    private IWxReturnPayService iWxReturnPayService;

    @Override
    public void onMessage(RefundPayProtocol protocol) {
        String tradeNo = protocol.getTradeNo();
        String transactionId = protocol.getTransactionId();
        try {
            LOGGER.info("[微信支付]-退款订单消费者. 微信支付单号：{}, 商户订单号：{}.", transactionId, tradeNo);
            RefundTradeRequest request = new RefundTradeRequest(transactionId, tradeNo);
            RefundOrder order = refundOrderClient.getOrder(request);
            //如果退款订单不存在或者不是退款中请求拒绝,不予退款
            if (StringUtils.isEmpty(order) || order.getStatus() != 1) {
                LOGGER.info("[微信支付]-退款订单消费者. 微信支付单号：{}, 商户订单号：{}.未查询到退款记录.无法进行退款.消费成功.", transactionId, tradeNo);
                return;
            }
            LOGGER.info("[微信支付]-退款订单消费者调用微信退款开始 微信支付单号：{}, 商户订单号：{}.", transactionId, tradeNo);
            iWxReturnPayService.returnPay(transactionId, tradeNo, order.getOutNo(), String.valueOf(order.getTotalFee()), String.valueOf(order.getRefundFee()));
        } catch (Exception e) {
            LOGGER.error("[微信支付]-退款订单消费者调用微信退款异常.正在重试. 微信支付单号：{}, 商户订单号：{}.", transactionId, tradeNo, e);
            String message = String.format("[订单超时消费者]-退款订单消费者调用微信退款异常.正在重试. 微信支付单号：%s, 商户订单号：%s.", transactionId, tradeNo);
            throw new RuntimeException(message, e);
        }
    }
}
