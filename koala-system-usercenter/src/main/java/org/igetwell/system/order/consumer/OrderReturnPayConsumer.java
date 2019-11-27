package org.igetwell.system.order.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.system.order.entity.Orders;
import org.igetwell.system.order.protocol.OrderProtocol;
import org.igetwell.system.order.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 退款订单消费者
 */
@Service
@RocketMQMessageListener(topic = "order-return-pay", consumerGroup = "order-return-pay", selectorExpression = "order-return-pay")
public class OrderReturnPayConsumer implements RocketMQListener<OrderProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderReturnPayConsumer.class);

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 微信支付成功回调,开始向MQ发送消息下单
     * 微信支付成功回调后订单状态是-1,1,2,3又进行了支付了的,说明的产生了重复支付的,都进行退款
     * 这种情况可能因为网络波动的问题造成重复支付,比如在网络不好的情况下支付了一次,没有立即显示支付成功,
     * 然后又进行支付.这样就造成了重复支付,需要全部给用户退款.
     * 订单状态: -1 订单超时 0待支付 1支付成功 2待退款订单 3已退款
     * @return
     */
    @Override
    public void onMessage(OrderProtocol protocol) {
        try {
            String orderNo = protocol.getOrderNo();
            LOGGER.info("[退款订单消费者：订单号={}, 商品ID={}, 手机号={}, 金额={}]", orderNo, protocol.getGoodsId(), protocol.getMobile(), protocol.getMoney().doubleValue());
            Orders order = redisUtils.get(orderNo);

            if (order == null) {

                LOGGER.info("[退款订单消费者]-根据订单号：{}未查询到订单记录,无法产生退款订单.", orderNo);
                String message = String.format("[退款订单消费者]-根据订单号：%s未查询到订单记录,无法产生退款订单.", orderNo);
                return;
                //throw new RuntimeException(message);
            }

            LOGGER.info("[退款订单消费者]-退款订单开始入库, 订单号={}.", orderNo);
            order.setPayStatus(2);//防止订单支付完成,写入数据时,订单超时延时队列改了状态是-1的时候,插入到数据库中的还是已超时.
            iOrderService.insert(order);
            LOGGER.info("[退款订单消费者]-退款订单入库成功, 订单号={}.", orderNo);
        } catch (Exception e){
            LOGGER.error("[退款订单消费者]-退款订单入库异常，正在重试, 订单号：{}.", protocol.getOrderNo(), e);
            String message = String.format("[退款订单消费者]-退款订单入库异常，正在重试, 订单号：%s.", protocol.getOrderNo());
            throw new RuntimeException(message, e);
        }
    }
}
