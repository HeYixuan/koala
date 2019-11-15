package org.igetwell.system.member.service.impl;

import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.RedisLock;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.order.entity.SeckillProduct;
import org.igetwell.system.member.mapper.SeckillProductMapper;
import org.igetwell.system.member.service.ISecKillProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 秒杀商品服务实现
 */
@Service
public class SecKillProductService implements ISecKillProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecKillProductService.class);

    @Resource
    SeckillProductMapper secKillProductMapper;

    @Autowired
    RedisLock redisLock;

    @Autowired
    RedisUtils redisUtils;


    @Override
    public List<SeckillProduct> getList() {
        List<SeckillProduct> list = secKillProductMapper.getList();
        if (list == null || list.size() <= 0) {
            throw new RuntimeException("请确保数据库中存在秒杀商品配置!");
        }
        list.stream().forEach(( product -> {
            String prodId = product.getProdId();
            redisUtils.set(prodId, product, 86400);
        }));
        LOGGER.info("[SecKillProductConfig]初始化秒杀配置完成,商品信息=[{}]", GsonUtils.toJson(list));
        return list;
    }

    @Override
    public SeckillProduct get(String id) {
        // 查缓存
        SeckillProduct product = (SeckillProduct) redisUtils.getObject(id);
        LOGGER.info("根据prodId=[{}],从缓存中获取产品信息:[{}]", id, product);
        if (product == null) {
            // 查库
            LOGGER.info("根据prodId=[{}],从缓存中获取产品信息为空,从数据库中查询", id);
            product = secKillProductMapper.get(id);
            // 回写缓存
            redisUtils.set(id, product, 86400);
        }
        LOGGER.info("根据prodId=[{}],查询到产品信息为:[{}]", id, GsonUtils.toJson(product));
        return product;
    }


    /**
     * 预减库存
     * @param prodId
     * @return
     */
    public boolean preReduceProdStock(String prodId) {
        boolean bool = redisLock.lock("reduce_stock_"+prodId, "30");
         if(bool){
            SeckillProduct product = (SeckillProduct) redisUtils.getObject(prodId);
            int prodStock = product.getProdStock();
            if (prodStock <= 0) {
                LOGGER.info("prodId={},prodStock={},当前秒杀商品库存已不足!", prodId, prodStock);
                return false;
            }

            int afterPreReduce = prodStock - 1;
            if (afterPreReduce < 0) {
                // 预减库存后小于0,说明库存预减失败,库存已不足
                LOGGER.info("prodId={} 预减库存失败,当前库存已不足!", prodId);
                return false;
            }
            // 预减库存成功,回写库存
            LOGGER.info("prodId={} 预减库存成功,当前扣除后剩余库存={}!", prodId, afterPreReduce);
            product.setProdStock(afterPreReduce);
            redisUtils.set(prodId, product, 86400);
            return true;
        }
        return false;
    }


    /**
     * 减库存,基于乐观锁
     * @param prodId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean reduceStock(String prodId) {
        LOGGER.info("[订单入库减库存]-减库存开始, 商品ID：{}", prodId);
        SeckillProduct product = get(prodId);
        if (product ==null || product.getProdStock() <= 0) {
            LOGGER.info("[订单入库减库存]-减库存失败. 商品信息：{}", GsonUtils.toJson(product));
            return false;
        }
        // 取库存
        int stock = product.getProdStock() - 1;
        product.setProdStock(stock);
        int count = 0;
        try {
            // 更新操作
            count = secKillProductMapper.update(product);
        } catch (Exception e) {
            LOGGER.error("[订单入库减库存] 商品ID：{},商品减库存异常,事务回滚,e={}", prodId, e.getStackTrace());
            String message = String.format("[订单入库减库存]商品ID：%s,商品减库存异常,事务回滚", prodId);
            throw new RuntimeException(message);
        }
        if (count != 1) {
            LOGGER.error("[订单入库减库存]商品ID：{},商品减库存[失败],事务回滚,e={}", prodId);
            String message = String.format("[订单入库减库存]商品ID：%s,商品减库存[失败],事务回滚", prodId);
            throw new RuntimeException(message);
        }
        LOGGER.info("[订单入库]当前减库存[成功],prodId={},剩余库存={}", prodId, stock);
        return true;
    }
}
