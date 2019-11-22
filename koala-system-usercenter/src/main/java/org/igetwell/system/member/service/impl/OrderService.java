package org.igetwell.system.member.service.impl;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.RedisLock;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.order.dto.OrderDto;
import org.igetwell.order.entity.SeckillOrder;
import org.igetwell.order.entity.SeckillProduct;
import org.igetwell.system.member.MessageProtocol;
import org.igetwell.system.member.SkillOrderMsgProtocol;
import org.igetwell.system.member.dto.request.ChargeOrderRequest;
import org.igetwell.system.member.dto.response.ChargeOrderResponse;
import org.igetwell.system.member.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * 订单服务：秒杀方案2
 */
@Service
public class OrderService implements IOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private SecKillProductService secKillProductService;

    @Autowired
    private SecKillChargeService secKillChargeService;

    @Autowired
    RedisLock redisLock;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    /**
     * 用户秒杀下单
     * 1.接口验签防刷
     * 2.前置参数校验
     * 3.商品校验,库存校验
     * 4.Lua预减库存,减库存成功,根据订单号缓存30分钟并生成秒杀日志记录和订单记录,提示用户支付
     * 5.用户未支付库存增加,支付成功回调发送MQ消息入队,消费者减真实库存
     * @param chargeOrderRequest
     * @return
     */
    public ResponseEntity createOrder(ChargeOrderRequest chargeOrderRequest){
        boolean bool = checkParam(chargeOrderRequest);
        if (!bool){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "参数错误!");
        }
        // 前置商品校验
        String prodId = chargeOrderRequest.getProdId();
        String mobile = chargeOrderRequest.getMobile();

        if (!checkMobileOrder(mobile, prodId)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "手机号已存在秒杀记录!");
        }

        SeckillProduct product = this.preCheck(prodId);
        if (product == null) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "商品信息不存在");
        }
        // 前置预减库存
        if (!this.reduceStock(prodId)) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "商品库存不足");
        }
        //设置金额,防止篡改金额
        chargeOrderRequest.setMoney(product.getProdPrice());
        return createOrderQueue(chargeOrderRequest);
    }

    /**
     * 订单入队,缓存30分钟并生成订单记录,提示用户支付
     * @param chargeOrderRequest
     * @return
     */
    public ResponseEntity createOrderQueue(ChargeOrderRequest chargeOrderRequest){
        // 订单号生成,组装秒杀订单消息协议
        String orderNo = UUID.randomUUID().toString();
        String mobile = chargeOrderRequest.getMobile();
        try {
            SkillOrderMsgProtocol protocol = new SkillOrderMsgProtocol(orderNo, mobile, chargeOrderRequest.getProdId(), chargeOrderRequest.getMoney());
            LOGGER.info("[秒杀订单消息投递开始], 开始入队.protocol={}", GsonUtils.toJson(protocol));
            ChargeOrderResponse response = new ChargeOrderResponse();
            BeanUtils.copyProperties(protocol, response);
            //写入缓存30分钟,可以使用异步
            writeCache(mobile, orderNo, protocol.getProdId(), protocol.getMoney());
            //异步投递延迟消息
            rocketMQTemplate.getProducer().setProducerGroup("order-check-timout");
            rocketMQTemplate.asyncSend("order-check-timout:order-check-timout", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                @Override
                public void onSuccess(SendResult var1) {
                    LOGGER.info("[秒杀订单异步延迟消息投递成功]: 投递结果{}", var1);
                }

                @Override
                public void onException(Throwable var1) {
                    LOGGER.error("[秒杀订单异步延迟消息投递失败]: 异常信息{}", var1);
                }

            }, 30000, 5);

            LOGGER.info("[秒杀订单异步延迟消息投递成功], 订单入队.response={},", GsonUtils.toJson(response));
            return ResponseEntity.ok(protocol);
        } catch (Exception e){
            int retryTimes = rocketMQTemplate.getProducer().getRetryTimesWhenSendAsyncFailed();
            LOGGER.error("[秒杀订单异步延迟消息投递异常], 手机号{}下单失败.正在重试第{}次", mobile, retryTimes, e);
        }
        return ResponseEntity.error();
    }



    /**
     * 前置参数校验
     * @param chargeOrderRequest
     * @return
     */
    private boolean checkParam(ChargeOrderRequest chargeOrderRequest){
        if (StringUtils.isEmpty(chargeOrderRequest)){
            return false;
        }
        String mobile = chargeOrderRequest.getMobile();
        String prodId = chargeOrderRequest.getProdId();
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(prodId)
                || chargeOrderRequest.getMoney() == null){
            LOGGER.info("秒杀下单必要参数不可为空");
            return false;
        }
        // 价格合法性校验 是否>0
        if (chargeOrderRequest.getMoney().longValue() < 0){
            LOGGER.info("商品交易金额小于0,价格非法,返回下单失败");
            return false;
        }
        return true;
    }

    /**
     * 秒杀下单前置商品校验
     * @param productId
     * @return
     */
    private SeckillProduct preCheck(String productId){
        // 商品校验
        SeckillProduct product = secKillProductService.getCache(productId);
        if (product == null) {
            LOGGER.info("prodId={},对应的商品信息不存在,返回下单失败", productId);
            return null;
        }
        return product;
    }


    /**
     * 从缓存预减库存
     * @param prodId
     * @return
     */
    public boolean reduceStock(String prodId) {

        boolean bool = redisLock.lock("reduce_stock_"+prodId, "30");
        if(bool){
            SeckillProduct product = (SeckillProduct) redisUtils.getObject(prodId);
            int prodStock = product.getProdStock();
            if (prodStock <= 0) {
                LOGGER.info("prodId={},prodStock={},当前秒杀商品库存已不足!", prodId, prodStock);
                return false;
            }
            int stock = prodStock - 1;
            if (stock < 0) {
                // 预减库存后小于0,说明库存预减失败,库存已不足
                LOGGER.info("prodId={} 预减库存失败,当前库存已不足!", prodId);
                return false;
            }
            // 预减库存成功,回写库存
            LOGGER.info("prodId={} 预减库存成功,当前扣除后剩余库存={}!", prodId, stock);
            product.setProdStock(stock);
            redisUtils.set(prodId, product, 86400);
            redisUtils.set("stock_"+prodId, stock);
            return true;
        }
        return false;
    }


    /**
     * 检查手机号和商品下单记录是否有已支付,存在禁止下单
     */
    private boolean checkMobileOrder(String mobile, String prodId){
        // 业务幂等:同一个商品ID和同一个mobile只有一个秒杀订单
        OrderDto changeOrderDto = new OrderDto(prodId, mobile);
        SeckillOrder changeOrder = secKillChargeService.getOrder(changeOrderDto);
        if (changeOrder != null  && changeOrder.getOrderStatus() == 0) {
            LOGGER.info("当前手机号={},秒杀的产品={}订单已存在,不得重复秒杀!", mobile, prodId);
            return false;
        }
        return true;
    }

    private void writeCache(String mobile, String orderNo, String prodId, BigDecimal money){
        //生成待支付订单记录
        SeckillOrder order = new SeckillOrder();
        order.setOrderNo(orderNo);
        order.setMobile(mobile);
        order.setProdId(prodId);
        order.setChargeMoney(money);
        order.setOrderStatus(1);
        order.setChargeTime(new Date());
        redisUtils.set(orderNo, order);
    }
}
