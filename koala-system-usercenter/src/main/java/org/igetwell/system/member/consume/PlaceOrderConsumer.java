package org.igetwell.system.member.consume;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.igetwell.order.dto.OrderDto;
import org.igetwell.order.entity.SeckillOrder;
import org.igetwell.order.entity.SeckillProduct;
import org.igetwell.system.member.SkillOrderMsgProtocol;
import org.igetwell.system.member.service.ISecKillChargeService;
import org.igetwell.system.member.service.ISecKillProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "SECKILL_CHARGE_ORDER_TOPIC", consumerGroup = "pay-test")
public class PlaceOrderConsumer implements RocketMQListener<SkillOrderMsgProtocol> {

    @Autowired
    private ISecKillChargeService iSecKillChargeService;

    @Autowired
    private ISecKillProductService iSecKillProductService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceOrderConsumer.class);

    @Override
    public void onMessage(SkillOrderMsgProtocol protocol) {
        LOGGER.info("[秒杀订单下单：订单号={}, 商品ID={}, 手机号={}, 金额={}]", protocol.getOrderNo(), protocol.getProdId(),
                protocol.getMobile(), protocol.getMoney().doubleValue());
        String orderNo = protocol.getOrderNo();
        String prodId= protocol.getProdId();
        String mobile= protocol.getMobile();
        // 消费幂等:查询orderId对应订单是否已存在
        SeckillOrder order = iSecKillChargeService.selectByOrderNo(orderNo);
        if (order != null) {
            LOGGER.info("[秒杀订单消费者]-当前订单已入库,不需要重复消费!,orderNo={}.", orderNo);
            //return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;  //TODO:onMessage方法如何接收消息被消费的
            return;
        }

        // 业务幂等:同一个商品ID和同一个mobile只有一个秒杀订单
        OrderDto changeOrderDto = new OrderDto(prodId, mobile);
        SeckillOrder changeOrder = iSecKillChargeService.getOrder(changeOrderDto);
        if (changeOrder != null) {
            LOGGER.info("[秒杀订单消费者]-当前手机号={},秒杀的产品={}订单已存在,不得重复秒杀, orderNo={}", mobile, prodId, orderNo);
            return;
        }
        // 库存校验
        SeckillProduct product = iSecKillProductService.get(prodId);
        if (product == null || product.getProdStock() <= 0){
            LOGGER.info("[秒杀订单消费者]当前商品已售罄,消息消费成功!prodId={},currStock={}", prodId, product.getProdStock());
            return;
        }
        // 正式下单
        if (iSecKillChargeService.chargeOrder(mobile, orderNo, prodId)) {
            LOGGER.info("[秒杀订单消费者]-秒杀订单入库成功,消息消费成功!, 订单号：{}", orderNo);
            // 模拟订单处理，直接修改订单状态为处理中
            order.setOrderStatus(2);  //TODO：待处理，等待支付
            iSecKillChargeService.update(order);
        }
    }
}
