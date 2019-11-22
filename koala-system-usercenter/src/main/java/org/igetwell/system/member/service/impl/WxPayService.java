package org.igetwell.system.member.service.impl;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.order.entity.SeckillOrder;
import org.igetwell.system.member.SkillOrderMsgProtocol;
import org.igetwell.system.member.dto.response.ChargeOrderResponse;
import org.igetwell.system.member.service.IWxPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 支付成功回调,处理待支付和超时订单支付
 */
@Service
public class WxPayService implements IWxPayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxPayService.class);
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 微信支付成功回调结束,开始向MQ发送消息下单
     * 订单状态，0 支付成功 1 待支付 2 订单失效 3 支付失败 4用户取消订单 5待退款订单 6已退款
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity createOrder(ChargeOrderResponse chargeOrderResponse){
        LOGGER.info("[订单支付成功,开始微信回调],入参信息：{}", GsonUtils.toJson(chargeOrderResponse));
        String orderNo = chargeOrderResponse.getOrderNo();
        String mobile = chargeOrderResponse.getMobile();
        String prodId = chargeOrderResponse.getProdId();
        try {
           SeckillOrder order = (SeckillOrder) redisUtils.getObject(orderNo);
           //这里用不用查数据库，存不存在订单记录
           //如果日志记录不为空,并且状态是待支付,说明有下单可以进行支付
            SkillOrderMsgProtocol protocol = new SkillOrderMsgProtocol(orderNo, mobile, prodId, chargeOrderResponse.getMoney());
           if (order != null && order.getOrderStatus().intValue() == 1){
               //redisUtils.del(orderNo);
               rocketMQTemplate.getProducer().setProducerGroup("order-pay");
               rocketMQTemplate.asyncSend("order-pay:pay-success", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                   @Override
                   public void onSuccess(SendResult sendResult) {
                       LOGGER.info("[微信支付], 异步投递消息成功创建订单,订单信息：{}, ", GsonUtils.toJson(protocol));
                       LOGGER.info("[微信支付], 异步投递消息成功创建订单,发送结果：{}, ", sendResult);
                   }
                   @Override
                   public void onException(Throwable throwable) {
                       LOGGER.error("[微信支付] 异步投递消息失败]: 异常信息{}", throwable);
                   }
               });
           }

            //如果日志记录不为空,并且状态是订单失效,说明有下单但已经超时,需要记录一条退款记录
            //如果日志记录不为空,并且状态是订单已支付,说明有重复支付,需要记录一条退款记录
           if(order != null && (order.getOrderStatus().intValue() == 2 || order.getOrderStatus().intValue() == 0)){
               //redisUtils.del(orderNo);
               rocketMQTemplate.getProducer().setProducerGroup("order-return-pay");
               rocketMQTemplate.asyncSend("order-return-pay:order-return-pay", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                   @Override
                   public void onSuccess(SendResult sendResult) {
                       LOGGER.info("[微信支付], 异步投递消息成功创建退款订单,订单信息：{}, ", GsonUtils.toJson(protocol));
                       LOGGER.info("[微信支付], 异步投递消息成功创建退款订单,发送结果：{}, ", sendResult);
                   }
                   @Override
                   public void onException(Throwable throwable) {
                       LOGGER.error("[微信支付] 异步投递消息失败]: 异常信息{}", throwable);
                   }
               });
           }
           return ResponseEntity.ok();
        } catch (Exception e){
            int retryTimes = rocketMQTemplate.getProducer().getRetryTimesWhenSendAsyncFailed();
            LOGGER.error("[微信支付] 异步投递消息异常]: 正在重试第{}次, 订单号：{}", retryTimes, orderNo);
        }
        return ResponseEntity.error();
    }
}
