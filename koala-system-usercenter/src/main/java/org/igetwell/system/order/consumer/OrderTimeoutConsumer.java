package org.igetwell.system.order.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.igetwell.common.constans.cache.RedisKey;
import org.igetwell.common.uitls.RedisUtil;
import org.igetwell.system.order.entity.Goods;
import org.igetwell.system.order.entity.Orders;
import org.igetwell.system.order.protocol.OrderProtocol;
import org.igetwell.system.order.service.IGoodsService;
import org.igetwell.system.order.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单超时队列
 */
@Service
@RocketMQMessageListener(topic = "order-timeout", consumerGroup = "order-timeout", selectorExpression = "order-timeout")
public class OrderTimeoutConsumer implements RocketMQListener<OrderProtocol> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderTimeoutConsumer.class);

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 订单超时,回滚库存
     * 订单状态: -1 订单超时 0待支付 1支付成功 2待退款订单 3已退款
     * @return
     */
    @Override
    public void onMessage(OrderProtocol protocol) {
        String orderNo = protocol.getOrderNo();
        Long goodsId= protocol.getGoodsId();
        Long mobile= protocol.getMobile();
        try {
            LOGGER.info("[订单超时消费者]-订单号={}, 商品ID={}, 手机号={}, 金额={}.", orderNo, goodsId, mobile, protocol.getMoney().doubleValue());
            Orders order = iOrderService.getOrderNo(orderNo);
            if (order != null){
                LOGGER.info("[订单超时消费者]-当前订单：{} 已入库,不需要重复消费.", orderNo);
                return;
            }
            order = redisUtil.get(orderNo);
            if (order != null && order.getPayStatus().intValue() == 0){
                order.setPayStatus(-1);
                Goods goods = redisUtil.get(String.valueOf(goodsId));
                if (goods == null){
                    LOGGER.error("[订单超时消费者]-从缓存中根据商品ID：{} 获取商品信息为空.", goodsId);
                    String message = String.format("[订单超时消费者]-从缓存中根据商品ID：{} 获取商品信息为空.", goodsId);
                    throw new RuntimeException(message);
                }
                int stock = goods.getStock();
                LOGGER.info("[订单超时消费者]-超时订单:{} 开始回滚库存, 当前剩余库存={}.", orderNo, stock);
                if (stock < 0) {
                    LOGGER.error("[订单超时消费者]-根据商品ID：{} 从缓存中预扣减库存失败, 当前商品库存不足.", goodsId);
                    String message = String.format("[订单超时消费者]-根据商品ID：%s 从缓存中预扣减库存失败.当前商品库存不足.", goodsId);
                    throw new RuntimeException(message);
                }
                int surplus = (goods.getStock() + 1);
                // 加库存成功,回写库存
                goods.setStock(surplus);
                redisUtil.set(String.valueOf(goodsId), goods, 86400);
                redisUtil.incr(String.format(RedisKey.COMPONENT_STOCK, goodsId));
                redisUtil.set(orderNo, order);
                LOGGER.info("[订单超时消费者]-超时订单:{} 回滚库存成功, 当前剩余库存={}.", orderNo, surplus);
                //iOrderService.insert(order);
                LOGGER.info("[订单超时消费者]-超时订单{} 入库成功.", orderNo);
            }
        } catch (Exception e){
            LOGGER.error("[订单超时消费者]-超时订单入库失败，正在重试, 订单号：{}.", protocol.getOrderNo(), e);
            String message = String.format("[订单超时消费者]-秒杀订单入库失败，正在重试, 订单号：%s.", protocol.getOrderNo());
            throw new RuntimeException(message, e);
        }
    }
}
