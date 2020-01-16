package org.igetwell.system.order.service.impl;

import org.igetwell.common.enums.OrderStatus;
import org.igetwell.common.enums.OrderType;
import org.igetwell.common.enums.PayChannel;
import org.igetwell.common.enums.TradeType;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.common.uitls.WebUtils;
import org.igetwell.paypal.dto.request.PayPalRequest;
import org.igetwell.paypal.feign.PayPalClient;
import org.igetwell.system.order.entity.TradeOrder;
import org.igetwell.system.order.service.IOrderCreateStrategy;
import org.igetwell.system.order.service.ITradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 创建银联支付订单
 */
@Service
public class UnionOrderStrategy implements IOrderCreateStrategy {

    @Autowired
    private Sequence sequence;
    @Autowired
    private ITradeOrderService iTradeOrderService;
    @Autowired
    private PayPalClient payPalClient;

    @Override
    public ResponseEntity createOrder(BigDecimal money) {
        TradeOrder orders = new TradeOrder(sequence.nextValue(), sequence.nextNo(), 10001L, "000000", OrderType.CONSUME.getValue(),
                13L, money, WebUtils.getIP(), OrderStatus.CREATE.getValue(), 10001L, "测试支付");
        PayPalRequest payPalRequest = new PayPalRequest(PayChannel.UNIONPAY, TradeType.NATIVE, orders.getTradeNo(), String.valueOf(orders.getGoodsId()), orders.getBody(), orders.getFee());
        iTradeOrderService.insert(orders);
        ResponseEntity<Map<String, String>> packageMap = payPalClient.scan(payPalRequest);
        if (StringUtils.isEmpty(packageMap) || packageMap.getStatus() != HttpStatus.OK.value()) {
            throw new RuntimeException("生成预支付订单失败.");
        }
        iTradeOrderService.updateOrderStatus(orders.getId(), OrderStatus.PENDING.getValue());
        return packageMap;
    }
}
