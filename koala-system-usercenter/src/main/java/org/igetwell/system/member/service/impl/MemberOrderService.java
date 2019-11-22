package org.igetwell.system.member.service.impl;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.order.entity.SeckillProduct;
import org.igetwell.system.member.MessageProtocol;
import org.igetwell.system.member.SkillOrderMsgProtocol;
import org.igetwell.system.member.dto.request.ChargeOrderRequest;
import org.igetwell.system.member.dto.response.ChargeOrderResponse;
import org.igetwell.system.member.service.IMemberOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.UUID;

/**
 * 订单服务：秒杀方案1
 */
@Service
public class MemberOrderService implements IMemberOrderService{

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberOrderService.class);

    @Autowired
    private SecKillProductService secKillProductService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    /**
     * 用户下单
     *  1.下单参数校验
     *  2.前置商品校验
     *  3.预减库存
     *  4.入队发送MQ消息
     * @return
     */
    @Override
    public ResponseEntity createOrder(ChargeOrderRequest chargeOrderRequest) {
        boolean bool = checkParam(chargeOrderRequest);
        if (!bool){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "参数错误!");
        }
        // 前置商品校验
        String prodId = chargeOrderRequest.getProdId();
        SeckillProduct product = this.check(prodId);
        if (product == null) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "商品信息不存在");
        }
        // 前置预减库存
        if (!secKillProductService.reduceStockCache(prodId)) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "商品库存不足");
        }
        //设置金额,防止篡改金额
        chargeOrderRequest.setMoney(product.getProdPrice());

        // 秒杀订单入队
       return this.createOrderQueue(chargeOrderRequest);
    }

    /**
     * 秒杀订单入队
     * @param chargeOrderRequest
     * @return
     */
    @Override
    public ResponseEntity createOrderQueue(ChargeOrderRequest chargeOrderRequest) {
        // 订单号生成,组装秒杀订单消息协议
        String orderNo = UUID.randomUUID().toString();
        String mobile = chargeOrderRequest.getMobile();
        try {
            SkillOrderMsgProtocol protocol = new SkillOrderMsgProtocol(orderNo, mobile, chargeOrderRequest.getProdId(), chargeOrderRequest.getMoney());
            LOGGER.info("[秒杀订单消息投递开始], 开始入队.protocol={}", GsonUtils.toJson(protocol));
            rocketMQTemplate.convertAndSend(MessageProtocol.SECKILL_ORDER_TOPIC.getTopic(), protocol);
            ChargeOrderResponse response = new ChargeOrderResponse();
            BeanUtils.copyProperties(protocol, response);
            LOGGER.info("[秒杀订单消息投递成功], 订单入队.response={},", GsonUtils.toJson(response));
            return ResponseEntity.ok(protocol);
        } catch (Exception e){
            int retryTimes = rocketMQTemplate.getProducer().getRetryTimesWhenSendFailed();
            LOGGER.error("[秒杀订单消息投递异常], 手机号{}下单失败.正在重试第{}次", mobile, retryTimes, e);
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
        if (StringUtils.isEmpty(chargeOrderRequest.getMobile()) || StringUtils.isEmpty(chargeOrderRequest.getProdId())
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
    private SeckillProduct check(String productId){
        // 商品校验
        SeckillProduct product = secKillProductService.getCache(productId);
        if (product == null) {
            LOGGER.info("prodId={},对应的商品信息不存在,返回下单失败", productId);
            return null;
        }
        return product;
    }

}
