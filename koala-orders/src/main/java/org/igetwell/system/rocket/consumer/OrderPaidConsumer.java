package org.igetwell.system.rocket.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.igetwell.system.goods.entity.Goods;
import org.igetwell.system.order.entity.Orders;
import org.igetwell.system.order.protocol.OrderProtocol;
import org.igetwell.system.goods.service.IGoodsService;
import org.igetwell.system.order.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付成功消费者
 */
@Service
@RocketMQMessageListener(topic = "order-pay", consumerGroup = "order-pay", selectorExpression = "pay-success")
public class OrderPaidConsumer implements RocketMQListener<OrderProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderPaidConsumer.class);

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IGoodsService iGoodsService;

    public void onMessage(OrderProtocol protocol) {
        try {
            String orderNo = protocol.getOrderNo();
            String memberId = protocol.getMemberId();
            Long goodsId= protocol.getGoodsId();
            Long mobile= protocol.getMobile();
            LOGGER.info("[订单支付成功消费者]-订单号={}, 商品ID={}, 手机号={}, 金额={}.", orderNo, goodsId, mobile, protocol.getMoney().doubleValue());
            // 消费幂等:查询orderNo对应订单是否已存在
            Orders order = iOrderService.getOrderNo(orderNo);
            if (order != null) {
                LOGGER.info("[订单支付成功消费者]-当前订单{}已入库,不需要重复消费.", orderNo);
                return;
            }
            // 业务幂等:同一个商品ID,用户ID,手机号只能有一个秒杀支付成功订单
            order = iOrderService.getOrder(memberId, mobile, goodsId);
            if (order != null) {
                LOGGER.info("[订单支付成功消费者]-当前用户：{}, 手机号：{}, 商品：{} 已存在成功支付订单,不得重复支付, 订单号：{}.", memberId, mobile, goodsId, orderNo);
                return;
            }
            // 库存校验
            Goods goods = iGoodsService.get(goodsId);
            if (goods == null || goods.getStock() <= 0){
                LOGGER.info("[订单支付成功消费者]-当前商品:{}已售罄,消息消费成功.", goodsId);
                return;
            }
            // 正式下单
            iOrderService.createOrder(orderNo, mobile, goodsId);
            LOGGER.info("[订单支付成功消费者]-订单入库成功,消息消费成功! 订单号：{}.", orderNo);
            //LOGGER.info("[秒杀订单消费者]-秒杀订单入库成功,删除缓存订单数据成功!, 订单号：{}", orderNo);
        } catch (Exception e){
            LOGGER.error("[订单支付成功消费者]-订单入库失败，正在重试, 订单号：{}.", protocol.getOrderNo());
            String message = String.format("[订单支付成功消费者]-订单入库失败，正在重试, 订单号：%s.", protocol.getOrderNo());
            throw new RuntimeException(message, e);
        }
    }
}
