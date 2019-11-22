package org.igetwell.system.order.service.impl;

import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.system.order.entity.Orders;
import org.igetwell.system.order.mapper.OrdersMapper;
import org.igetwell.system.order.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService implements IOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Resource
    private OrdersMapper ordersMapper;

    @Override
    public Orders getOrderNo(String orderNo) {
        LOGGER.info("[订单服务]-根据订单号：{} 查询订单开始.", orderNo);
        Orders orders = ordersMapper.getOrderNo(orderNo);
        LOGGER.info("[订单服务]-根据订单号: {}查询订单结束.", orderNo);
        return orders;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(Orders order) {
        try {
            int i = ordersMapper.insert(order);
            if (i <= 0){
                LOGGER.info("[订单服务]-插入订单数据失败，回滚数据", GsonUtils.toJson(order));
                throw new RuntimeException("[订单服务]-插入订单数据失败，回滚数据");
            }
        } catch (Exception e){
            LOGGER.info("[订单服务]-插入订单数据异常，回滚数据", GsonUtils.toJson(order));
            throw new RuntimeException("[订单服务]-插入订单数据异常，回滚数据");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Orders order) {
        try {
            int i = ordersMapper.update(order);
            if (i <= 0){
                LOGGER.info("[订单服务]-修改订单数据失败，回滚数据", GsonUtils.toJson(order));
                throw new RuntimeException("[订单服务]-修改订单数据失败，回滚数据");
            }
        } catch (Exception e){
            LOGGER.info("[订单服务]-修改订单数据异常，回滚数据", GsonUtils.toJson(order));
            throw new RuntimeException("[订单服务]-修改订单数据异常，回滚数据");
        }
    }

    @Override
    public void deleteById(Long id) {
        LOGGER.info("[订单服务]-删除订单数据,订单ID: {}", id);
        ordersMapper.deleteById(id);
    }
}
