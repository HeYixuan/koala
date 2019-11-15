package org.igetwell.system.member.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.order.dto.OrderDto;
import org.igetwell.order.entity.SeckillOrder;
import org.igetwell.order.entity.SeckillProduct;
import org.igetwell.system.member.dto.request.ChargeOrderRequest;
import org.igetwell.system.member.mapper.SeckillOrderMapper;
import org.igetwell.system.member.service.ISecKillChargeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * 秒杀下单service实现
 */
@Service
public class SecKillChargeService implements ISecKillChargeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecKillChargeService.class);

    @Resource
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SecKillProductService secKillProductService;


    /**
     * 查询订单信息
     */
    public SeckillOrder selectByOrderNo(String orderNo){
        LOGGER.info("[查询订单信息]-内部订单根据订单号查询开始,订单号：{}", orderNo);
        SeckillOrder order = seckillOrderMapper.selectByOrderNo(orderNo);
        LOGGER.info("[查询订单信息]-内部订单根据订单号查询结束,订单信息：{}", GsonUtils.toJson(order));
        return seckillOrderMapper.selectByOrderNo(orderNo);
    }

    /**
     * 查询订单信息
     * 根据手机号和商品ID
     */
    public SeckillOrder getOrder(OrderDto dto){
        LOGGER.info("[查询订单信息]-内部订单根据商品ID和手机号查询开始,入参={}", GsonUtils.toJson(dto));
        SeckillOrder order = seckillOrderMapper.getOrder(dto);
        LOGGER.info("[查询订单信息]-内部订单根据商品ID和手机号查询结束,订单信息：{}", GsonUtils.toJson(order));
        return order;
    }

    public int delete(Integer id){
        return seckillOrderMapper.delete(id);
    }

    public int insert(SeckillOrder order){
        return seckillOrderMapper.insert(order);
    }

    public int update(SeckillOrder order){
        return seckillOrderMapper.update(order);
    }


    /**
     * 秒杀订单入库
     * @param mobile 手机号
     * @param orderNo 订单号
     * @param productId 商品ID
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean chargeOrder(String mobile, String orderNo, String productId) {
        int count = 0;
        // 减库存
        if (!secKillProductService.reduceStock(productId)) {
            LOGGER.info("[秒杀订单入库] 订单号={}, 商品ID={},下单前减库存失败,下单失败!", orderNo, productId);
            // TODO 此处可给用户发送通知，告知秒杀下单失败，原因：商品已售罄
            return false;
        }
        // 设置产品名称
        SeckillProduct product = secKillProductService.get(productId);
        SeckillOrder order = new SeckillOrder();
        order.setOrderNo(orderNo);
        order.setProdId(productId);
        order.setMobile(mobile);
        order.setChargeMoney(product.getProdPrice());
        order.setOrderStatus(2);
        order.setChargeTime(new Date());
        order.setProdName(product.getProdName());
        try {
            count = insert(order);
        } catch (Exception e) {
            LOGGER.error("[秒杀订单入库]-订单号：{},入库异常 事务执行回滚", orderNo, e.getStackTrace());
            String message = String.format("[秒杀订单入库]-订单号：%s,入库异常, 事务执行回滚", orderNo);
            throw new RuntimeException(message);
        }
        if (count != 1) {
            LOGGER.error("[秒杀订单入库]-订单号：{},入库失败 事务执行回滚", orderNo);
            String message = String.format("[秒杀订单入库]-订单号：%s,入库失败, 事务执行回滚", orderNo);
            throw new RuntimeException(message);
        }
        return true;
    }

}
