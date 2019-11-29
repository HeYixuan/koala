package org.igetwell.system.order.service.impl;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.dto.request.OrderPayRequest;
import org.igetwell.system.order.entity.Orders;
import org.igetwell.system.order.protocol.OrderProtocol;
import org.igetwell.system.order.service.IOrderService;
import org.igetwell.system.order.service.IWxPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class WxPayService implements IWxPayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxPayService.class);

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 微信支付成功回调,开始向MQ发送消息下单
     * 订单状态: -1 订单超时 0待支付 1支付成功 2待退款订单 3已退款
     * TODO:需要处理订单还没超时,支付成功了，恰巧异步延迟消息收到消息订单超时了,库存回滚了,订单状态被修改为-1订单超时
     * TODO: 1.订单超时了,支付成功了,产生退款记录 2.订单没超时支付成功,正在修改数据的时候缓存里面的状态被改，造成插入订单数据失败.
     * @return
     */
    @Override
    public ResponseEntity createOrder(OrderPayRequest request) {
        LOGGER.info("[微信支付回调]-订单支付成功,开始微信回调,入参信息：{}.", GsonUtils.toJson(request));
        String orderNo = request.getOrderNo();
        Long memberId = request.getMemberId();
        Long mobile = request.getMobile();
        Long goodsId = request.getGoodsId();
        BigDecimal money = request.getMoney();

        try {
            Orders order = iOrderService.getOrderNo(orderNo);
            if (order != null) {
                LOGGER.info("[微信支付回调]-当前用户：{}, 手机号：{}, 商品：{} 已存在订单记录,不需要重复支付, 订单号：{}.", memberId, mobile, goodsId, orderNo);
                return ResponseEntity.error(HttpStatus.TOO_MANY_REQUESTS);
            }
            order = redisUtils.get(orderNo);
            //这里用不用查数据库，存不存在订单记录
            //如果日志记录不为空,并且状态是待支付,说明有下单可以进行支付
            OrderProtocol protocol = new OrderProtocol(orderNo, memberId, mobile, goodsId, money);

            if (order != null && order.getPayStatus() != null) {
                order.setTradeNo(System.nanoTime());
                order.setPayTime(new Date());
                if (order.getPayStatus().intValue() == 0) {
                    order.setPayStatus(1);
                    rocketMQTemplate.getProducer().setProducerGroup("order-pay");
                    rocketMQTemplate.asyncSend("order-pay:pay-success", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                        @Override
                        public void onSuccess(SendResult var) {
                            LOGGER.info("[微信支付回调]-异步投递创建订单消息成功,订单信息：{}. ", GsonUtils.toJson(protocol));
                            LOGGER.info("[微信支付回调]-异步投递创建订单消息成功,投递结果：{}. ", var);
                        }
                        @Override
                        public void onException(Throwable var) {
                            LOGGER.error("[微信支付回调]-异步创建订单投递消息失败]: 异常信息：{}.", var);
                        }
                    });
                }

                //如果日志记录不为空,并且状态是订单不是待支付,说明有重复支付,需要记录一条退款记录,
                //防止用户一直挂起支付界面,到半小时后支付的情况一律按退款单处理
                if (order.getPayStatus().intValue() != 0 && order.getPayStatus().intValue() != 1) {
                    order.setPayStatus(2);
                    rocketMQTemplate.getProducer().setProducerGroup("order-return-pay");
                    rocketMQTemplate.asyncSend("order-return-pay:order-return-pay", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                        @Override
                        public void onSuccess(SendResult var) {
                            LOGGER.info("[微信支付回调]-异步投递退款订单消息成功,订单信息：{}.", GsonUtils.toJson(protocol));
                            LOGGER.info("[微信支付回调]-异步投递退款订单消息成功,发送结果：{}.", var);
                        }
                        @Override
                        public void onException(Throwable var) {
                            LOGGER.error("[微信支付回调]-异步投递退款订单消息失败]: 异常信息：{}.", var);
                        }
                    });
                }
                redisUtils.set(orderNo, order);
                return ResponseEntity.ok();
            }
        } catch (Exception e){
            int retryTimes = rocketMQTemplate.getProducer().getRetryTimesWhenSendAsyncFailed();
            LOGGER.error("[微信支付回调]-异步投递消息异常]: 正在重试第{}次, 订单号：{}.", retryTimes, orderNo);
        }
        return ResponseEntity.error(HttpStatus.FORBIDDEN);
    }
}
