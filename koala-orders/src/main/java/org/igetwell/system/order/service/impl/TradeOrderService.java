package org.igetwell.system.order.service.impl;

import org.igetwell.common.enums.OrderStatus;
import org.igetwell.common.enums.OrderType;
import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.common.uitls.WebUtils;
import org.igetwell.paypal.dto.request.PayPalRequest;
import org.igetwell.paypal.feign.PayPalClient;
import org.igetwell.system.order.dto.request.OrderPay;
import org.igetwell.system.order.entity.TradeOrder;
import org.igetwell.system.order.mapper.TradeOrderMapper;
import org.igetwell.system.order.service.ITradeOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class TradeOrderService implements ITradeOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Resource
    private TradeOrderMapper tradeOrderMapper;

    @Autowired
    private PayPalClient payPalClient;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private Sequence sequence;

    @Override
    public TradeOrder get(Long id) {
        LOGGER.info("[支付订单服务]-根据主键：{} 查询支付订单开始.", id);
        TradeOrder orders = tradeOrderMapper.get(id);
        LOGGER.info("[支付订单服务]-根据主键：{} 查询支付订单结束.", id);
        return orders;
    }

    /**
     * 根据商户订单号获取缓存订单数据
     * @param orderNo
     * @return
     */
    @Override
    public TradeOrder getCache(String orderNo) {
        TradeOrder orders = redisUtils.get(orderNo);
        LOGGER.info("[支付订单服务]-根据商户订单号：{} 从缓存中查询支付订单信息开始.", orderNo);
        if (orders == null) {
            LOGGER.error("[支付订单服务]-根据商户订单号：{} 从缓存中查询商品信息为空.", orderNo);
            String message = "[支付订单服务]-根据商户订单号：从缓存中查询商品信息为空.";
            throw new RuntimeException(String.format(message, orderNo));
        }
        LOGGER.info("[支付订单服务]-根据商户订单号: {} 查询到支付订单信息: [{}].", orders, GsonUtils.toJson(orders));
        return orders;
    }

    @Override
    public void deleteById(Long id) {
        LOGGER.info("[支付订单服务]-删除支付订单数据. ID：{}.", id);
        tradeOrderMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(TradeOrder order) {
        LOGGER.info("[支付订单服务]-支付订单入库开始.");
        int i = tradeOrderMapper.insert(order);
        if (i <= 0) {
            LOGGER.error("[支付订单服务]-支付订单入库失败,微信支付单号：{}, 商户订单号：{} 事务执行回滚.", order.getTransactionId(), order.getTradeNo());
            String message = String.format("[订单服务]-支付订单入库失败，微信支付单号：%s, 商户订单号：%s 事务执行回滚.", order.getTransactionId(), order.getTradeNo());
            throw new RuntimeException(message);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TradeOrder order) {
        LOGGER.info("[支付订单服务]-修改支付订单开始.", order.getId());
        int i = tradeOrderMapper.update(order);
        if (i <= 0) {
            LOGGER.info("[支付订单服务]-修改订单{}数据失败，事务执行回滚.", order.getId());
            String message = String.format("[支付订单服务]-修改订单%s数据失败，事务执行回滚.", order.getId());
            throw new RuntimeException(message);
        }
        LOGGER.info("[支付订单服务]-修改支付订单结束.");
    }

    public ResponseEntity trade(OrderPay orderPay) {
        TradeOrder orders = new TradeOrder(sequence.nextValue(), sequence.nextNo(), orderPay.getMchId(), orderPay.getMchNo(), orderPay.getOrderType().getValue(),
                orderPay.getChannelId(), orderPay.getFee(), WebUtils.getIP(), OrderStatus.CREATE.getValue(), orderPay.getGoodsId(), orderPay.getBody());
        this.insert(orders);
        PayPalRequest payPalRequest = new PayPalRequest();
        payPalRequest.setGoodsId(orders.getGoodsId());
        payPalRequest.setFee(orders.getFee());
        payPalRequest.setBody(orders.getBody());
        Map<String, String> resultMap = payPalClient.aliPay(payPalRequest);
        return ResponseEntity.ok(resultMap);
    }
}
