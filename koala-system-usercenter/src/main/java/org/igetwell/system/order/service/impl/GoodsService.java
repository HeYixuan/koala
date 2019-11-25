package org.igetwell.system.order.service.impl;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.common.constans.cache.RedisKey;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.RedisUtil;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.dto.request.ChargeOrderRequest;
import org.igetwell.system.order.entity.Goods;
import org.igetwell.system.order.entity.Orders;
import org.igetwell.system.order.mapper.GoodsMapper;
import org.igetwell.system.order.protocol.OrderProtocol;
import org.igetwell.system.order.service.IGoodsService;
import org.igetwell.system.order.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsService implements IGoodsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsService.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private GoodsMapper goodsMapper;
    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedisUtil redisUtil;


    public List<Goods> getList() {
        LOGGER.info("[商品服务]-初始化秒杀的商品列表开始.");
        List<Goods> goodsList = goodsMapper.getList();
        /*goodsList.forEach(goods -> {

        });
        goodsList.stream().forEach((goods -> {
            Long goodsId = goods.getId();
        }));*/
        LOGGER.info("[商品服务]-初始化秒杀的商品列表结束. 商品信息:{}.", GsonUtils.toJson(goodsList));
        return goodsList;
    }


    @Override
    public Goods get(Long id) {
        LOGGER.info("[商品服务]-根据商品ID：{} 查询商品信息开始.", id);
        Goods goods = goodsMapper.get(id);
        LOGGER.info("[商品服务]-根据订单号: {}查询商品信息结束.", id);
        return goods;
    }

    @Override
    public Goods getCache(Long id) {
        Goods goods = redisUtil.get(String.valueOf(id));
        LOGGER.info("[商品服务]-根据商品ID：{} 从缓存中查询商品信息: [{}].", id, GsonUtils.toJson(goods));
        if (goods == null) {
            LOGGER.info("[商品服务]-根据商品ID：{} 从缓存中查询商品信息为空. 开始查询数据库.", id);
            goods = this.get(id);
        }
        LOGGER.info("[商品服务]-根据商品ID: {} 查询到商品信息: [{}].", id, GsonUtils.toJson(goods));
        return goods;
    }

    /**
     * 从缓存预减库存
     *
     * @param goodsId
     * @return
     */
    public boolean reduceStock(Long goodsId) {
        try {
            LOGGER.info("[商品服务]-根据商品ID：{} 从缓存中开始预扣减库存.");
            boolean bool = redisUtil.lock(String.format(RedisKey.DECR_STOCK_LOCK, goodsId), "10");
            if (!bool) {
                LOGGER.info("[商品服务]-排队人数太多，请稍后再试.");
                return false;
            }
            Goods goods = this.getCache(goodsId); //这里缓存没有,取数据库的时候会不会有问题,因为别人支付了,库存还没减的,这样库存会不会是超卖的现象
            if (!checkObject(goods) || goods.getStock() == null || goods.getStock() <= 0) {
                LOGGER.info("[商品服务]-根据商品ID：{} 从缓存中预扣减库存失败, 当前商品库存不足.当前库存数:{}.", goodsId, goods.getStock());
                return false;
            }
            int stock = goods.getStock();
            int surplus = (stock - 1);
            if (surplus < 0) {
                LOGGER.info("[商品服务]-根据商品ID：{} 从缓存中预扣减库存失败, 当前商品库存不足.", goodsId);
                return false;
            }
            redisUtil.set(String.format(RedisKey.COMPONENT_STOCK, goodsId), stock);
            // 预减库存成功,回写库存
            goods.setStock(surplus);
            redisUtil.set(String.valueOf(goodsId), goods, 86400);
            redisUtil.set(String.valueOf(goodsId), goods, 86400);
            redisUtil.decr(String.format(RedisKey.COMPONENT_STOCK, goodsId));
            LOGGER.info("[商品服务]-根据商品ID：{} 从缓存中预扣减库存成功,当前扣除后剩余库存数:{}.", goodsId, surplus);
            return true;
        } catch (Exception e) {
            redisUtil.unlock(String.format(RedisKey.DECR_STOCK_LOCK, goodsId));
            return false;
        }

    }

    /**
     * 从数据库减库存
     *
     * @param goodsId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean reduceGoodsStock(Long goodsId) {
        try {
            LOGGER.info("[商品服务]-根据商品ID：{} 从数据库中减库存开始.", goodsId);
            Goods goods = this.get(goodsId);
            if (goods == null || goods.getStock() == null || goods.getStock() <= 0) {
                LOGGER.info("[商品服务]-根据商品ID：{} 从数据库中扣减库存失败, 当前商品库存不足.当前库存数:{}.", goodsId, goods.getStock());
                return false;
            }
            int surplus = (goods.getStock() - 1);
            if (surplus < 0) {
                LOGGER.info("[商品服务]-根据商品ID：{} 从数据库中扣减库存失败, 当前商品库存不足.", goodsId);
                return false;
            }
            //减库存成功,回写库存
            goods.setStock(surplus);
            this.update(goods);
            redisUtil.set(String.valueOf(goodsId), goods, 86400);
            LOGGER.info("[商品服务]-根据商品ID：{} 从数据库中扣减库存成功,当前扣除后剩余库存数:{}.", goodsId, surplus);
            return true;
        } catch (Exception e) {
            redisUtil.incr(String.format(RedisKey.COMPONENT_STOCK, goodsId));
            LOGGER.error("[商品服务]-商品ID：{},商品减库存失败, 发生异常, 事务执行回滚,e={}.", goodsId, e.getStackTrace());
            String message = String.format("[商品服务]商品ID：%s,商品减库存失败,发生异常,事务执行回滚.", goodsId);
            throw new RuntimeException(message, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Goods goods) {
        try {
            int i = goodsMapper.update(goods);
            if (i <= 0) {
                LOGGER.info("[商品服务]-修改商品{}数据失败，事务执行回滚.", goods.getId());
                String message = String.format("[商品服务]-修改商品%s数据失败，事务执行回滚.", goods.getId());
                throw new RuntimeException(message);
            }
        } catch (Exception e) {
            LOGGER.info("[商品服务]-商品{}数据失败，事务执行回滚.", goods.getId());
            String message = String.format("[商品服务]-商品%s数据失败，事务执行回滚.", goods.getId());
            throw new RuntimeException(message, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(Goods goods) {
        try {
            int i = goodsMapper.insert(goods);
            if (i <= 0) {
                LOGGER.error("[商品服务]-商品ID：{},入库失败, 事务执行回滚.", goods.getId());
                String message = String.format("[商品服务]-商品ID：{},入库失败, 事务执行回滚.", goods.getId());
                throw new RuntimeException(message);
            }
        } catch (Exception e) {
            LOGGER.error("[商品服务]-商品ID：{},入库失败, 发生异常, 事务执行回滚.", goods.getId());
            String message = String.format("[商品服务]-商品ID：{},入库失败, 发生异常, 事务执行回滚.", goods.getId());
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public void deleteById(Long id) {
        LOGGER.info("[商品服务]-删除商品数据,商品ID: {}.", id);
        goodsMapper.deleteById(id);
    }


    /**
     * 秒杀下单服务
     *
     * @param request
     * @return
     */
    @Override
    public ResponseEntity createOrder(ChargeOrderRequest request) {
        if (!this.checkParam(request)) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "参数错误!");
        }
        Long goodsId = request.getGoodsId();
        Long mobile = request.getMobile();
        String memberId = request.getMemberId();
        if (!this.iOrderService.checkOrderPay(memberId, mobile, goodsId)) {
            return ResponseEntity.error(HttpStatus.TOO_MANY_REQUESTS, "不能重复进行下单!");
        }

        Goods goods = this.getCache(goodsId);
        if (!this.checkObject(goods)) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "商品信息不存在!");
        }
        if (!this.reduceStock(goodsId)) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "商品库存不足无法下单!");
        }
        //防止篡改金额
        request.setMoney(goods.getSalesPrice());
        return this.createOrderQueue(request);
    }


    /**
     * 订单入队,缓存30分钟并生成订单记录,提示用户支付
     *
     * @param request
     * @return
     */
    @Override
    public ResponseEntity createOrderQueue(ChargeOrderRequest request) {
        String orderNo = String.valueOf(System.nanoTime());
        Long mobile = request.getMobile();
        Long goodsId = request.getGoodsId();
        String memberId = request.getMemberId();
        BigDecimal money = request.getMoney();
        try {

            OrderProtocol protocol = new OrderProtocol(orderNo, memberId, mobile, goodsId, request.getMoney());
            LOGGER.info("[秒杀订单消息投递开始], 开始入队.protocol={}", GsonUtils.toJson(protocol));
            //ChargeOrderResponse response = new ChargeOrderResponse();
            //BeanUtils.copyProperties(protocol, response);
            //写入缓存30分钟,可以使用异步双写
            writeCache(memberId, orderNo, mobile, goodsId, money);
            //异步投递延迟消息
            rocketMQTemplate.getProducer().setProducerGroup("order-check-timout");
            rocketMQTemplate.asyncSend("order-check-timout:order-check-timout", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                @Override
                public void onSuccess(SendResult var) {
                    LOGGER.info("[秒杀订单异步延迟消息]-投递延迟消息成功,投递结果：{}.", var);
                }

                @Override
                public void onException(Throwable var) {
                    LOGGER.error("[秒杀订单异步延迟消息]:-投递延迟消息失败,异常信息：{}.", var);
                }

            }, 30000, 3);

            LOGGER.info("[秒杀订单异步延迟消息], 订单入队.response={}.", GsonUtils.toJson(protocol));
            return ResponseEntity.ok(protocol);
        } catch (Exception e) {
            int retryTimes = rocketMQTemplate.getProducer().getRetryTimesWhenSendAsyncFailed();
            LOGGER.error("[秒杀订单异步延迟消息投递异常], 手机号{}下单失败.正在重试第{}次.", mobile, retryTimes, e);
        }
        return ResponseEntity.error();
    }


    /**
     * 前置参数校验
     *
     * @param request
     * @return
     */
    private boolean checkParam(ChargeOrderRequest request) {
        if (StringUtils.isEmpty(request)) {
            return false;
        }
        Long mobile = request.getMobile();
        Long goodsId = request.getGoodsId();
        String memberId = request.getMemberId();
        if (StringUtils.isEmpty(memberId) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(goodsId)) {
            LOGGER.info("[商品服务]-秒杀下单必要参数不可为空.");
            return false;
        }
        return true;
    }

    private boolean checkObject(Object object) {
        if (object == null) {
            return false;
        }
        return true;
    }

    /**
     * 生成待支付订单记录
     *
     * @param memberId
     * @param orderNo
     * @param mobile
     * @param goodsId
     * @param money
     */
    private void writeCache(String memberId, String orderNo, Long mobile, Long goodsId, BigDecimal money) {
        Orders order = new Orders();
        order.setId(System.nanoTime());
        order.setMemberId(memberId);
        order.setOrderNo(orderNo);
        order.setMobile(mobile);
        order.setGoodsId(goodsId);
        order.setPayAmount(money);
        order.setOrderTime(new Date());
        order.setPayStatus(0);
        redisUtil.set(orderNo, order);
    }
}
