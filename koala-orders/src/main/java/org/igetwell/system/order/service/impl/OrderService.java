package org.igetwell.system.order.service.impl;

import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.system.order.entity.Orders;
import org.igetwell.system.order.mapper.OrdersMapper;
import org.igetwell.system.goods.service.IGoodsService;
import org.igetwell.system.order.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service(value = "ordersService")
@Transactional(rollbackFor = Exception.class)
public class OrderService implements IOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Resource
    private OrdersMapper ordersMapper;

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Orders getOrderNo(String orderNo) {
        LOGGER.info("[订单服务]-根据订单号：{} 查询订单开始.", orderNo);
        Orders orders = ordersMapper.getOrderNo(orderNo);
        LOGGER.info("[订单服务]-根据订单号: {} 查询订单结束.", orderNo);
        return orders;
    }

    @Override
    public Orders getOrder(Long memberId, Long mobile, Long goodsId) {
        LOGGER.info("[订单服务]-根据用户ID：{}, 手机号：{}, 商品ID：{} 查询订单开始.", memberId, mobile, goodsId);
        Orders orders = ordersMapper.getOrder(memberId, mobile, goodsId);
        LOGGER.info("[订单服务]-根据用户ID：{}, 手机号：{}, 商品ID：{} 查询订单结束.", memberId, mobile, goodsId);
        return orders;
    }

    /**
     * 查询会员所有订单
     * @param memberId
     * @return
     */
    public Orders getMemberOrder(Long memberId) {
        LOGGER.info("[订单服务]-根据用户ID：{} 查询订单开始.", memberId);
        Orders orders = ordersMapper.getMemberOrder(memberId);
        LOGGER.info("[订单服务]-根据用户ID：{} 查询订单结束.", memberId);
        return orders;
    }

    @Override
    public Orders getCache(String orderNo) {
        Orders order = redisUtils.get(orderNo);
        LOGGER.info("[订单服务]-根据订单号：{} 从缓存中查询订单信息: [{}].", orderNo, GsonUtils.toJson(order));
        if (order == null) {
            LOGGER.info("[订单服务]-根据订单号：{} 从缓存中查询订单信息为空. 开始查询数据库.");
            order = this.getOrderNo(orderNo);
        }
        LOGGER.info("[订单服务]-根据订单号: {} 查询到订单信息: [{}].", orderNo, GsonUtils.toJson(order));
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(Orders order) {
        try {
            int i = ordersMapper.insert(order);
            if (i <= 0) {
                LOGGER.error("[订单服务]-订单号：{},入库失败, 事务执行回滚.", order.getOrderNo());
                String message = String.format("[订单服务]-订单号：%s,入库失败, 事务执行回滚.", order.getOrderNo());
                throw new RuntimeException(message);
            }
        } catch (Exception e) {
            LOGGER.error("[订单服务]-订单号：{},入库失败, 发生异常, 事务执行回滚.", order.getOrderNo());
            String message = String.format("[订单服务]-订单号：%s,入库失败, 发生异常, 事务执行回滚.", order.getOrderNo());
            throw new RuntimeException(message, e);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Orders order) {
        try {
            int i = ordersMapper.update(order);
            if (i <= 0) {
                LOGGER.info("[订单服务]-修改订单{}数据失败，事务执行回滚.", order.getOrderNo());
                String message = String.format("[订单服务]-修改订单%s数据失败，事务执行回滚.", order.getOrderNo());
                throw new RuntimeException(message);
            }
        } catch (Exception e) {
            LOGGER.info("[订单服务]-修改订单{}数据失败, 发生异常, 事务执行回滚.", order.getOrderNo());
            String message = String.format("[订单服务]-修改订单%s数据失败, 发生异常, 事务执行回滚.", order.getOrderNo());
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public void deleteById(Long id) {
        LOGGER.info("[订单服务]-删除订单数据,订单ID: {}", id);
        ordersMapper.deleteById(id);
    }

    /**
     * 订单扣减库存并入库
     * @param orderNo
     * @param mobile
     * @param goodsId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createOrder(String orderNo, Long mobile, Long goodsId) {
        try {
            LOGGER.info("[订单服务]-开始创建订单, 订单号：{}, 手机号：{}, 商品ID：{}", orderNo, mobile, goodsId);
            if (!this.iGoodsService.reduceGoodsStock(goodsId)) {
                LOGGER.info("[订单服务]-订单号={}, 商品ID={},下单前减库存失败,创建订单失败.", orderNo, goodsId);
                // TODO 此处可给用户发送通知，告知秒杀下单失败，原因：商品已售罄
                return false;
            }
            Orders order = redisUtils.get(orderNo);
            //如果在支付成功回调里面没有写入订单数据,或者支付成功并没有改变支付成功状态,无法创建订单.
            if (order == null) {
                LOGGER.info("[订单服务]-无法获取订单数据,创建订单失败.订单号：{}, 手机号：{}, 商品ID：{}.", orderNo, mobile, goodsId);
                String message = String.format("[订单服务]-无法获取订单数据,创建订单失败.订单号：%s.", orderNo);
                throw new RuntimeException(message);
            }
            order.setPayStatus(1); //防止订单支付完成,写入数据时,订单超时延时队列改了状态是-1的时候,插入到数据库中的还是已超时.
            this.insert(order);
            return true;
        } catch (Exception e) {
            LOGGER.error("[订单服务]-订单号：{},入库异常 事务执行回滚", orderNo, e.getStackTrace());
            String message = String.format("[订单服务]-订单号：%s,入库异常, 事务执行回滚", orderNo);
            throw new RuntimeException(message, e);
        }
    }


    /**
     * 检查是否有支付订单记录
     * @param memberId
     * @param mobile
     * @param goodsId
     * @return
     */
    public boolean checkOrderPay(Long memberId, Long mobile, Long goodsId) {
        Orders order = ordersMapper.getOrder(memberId, mobile, goodsId);
        if (order == null) {
            return true;
        }
        return false;
    }

}
