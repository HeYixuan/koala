package org.igetwell.system.order.service.impl;

import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.system.order.entity.Goods;
import org.igetwell.system.order.mapper.GoodsMapper;
import org.igetwell.system.order.service.IGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsService implements IGoodsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsService.class);

    @Resource
    private GoodsMapper goodsMapper;

    @Autowired
    private RedisUtils redisUtils;


    public List<Goods> getList(){
        LOGGER.info("[商品服务]-初始化秒杀的商品列表开始.");
        List<Goods> goodsList = goodsMapper.getList();
        goodsList.forEach( goods -> {

        });
        goodsList.stream().forEach(( goods -> {
            Long goodsId = goods.getId();
        }));
        LOGGER.info("[商品服务]-初始化秒杀的商品列表结束. 商品信息", GsonUtils.toJson(goodsList));
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
        Goods goods = (Goods) redisUtils.getObject(String.valueOf(id));
        LOGGER.info("[商品服务]-根据商品ID：{} 从缓存中查询商品信息: [{}].", id , GsonUtils.toJson(goods));
        if (goods == null) {
            LOGGER.info("[商品服务]-根据商品ID：{} 从缓存中查询商品信息为空. 开始查询数据库");
            goods = this.get(id);
        }
        LOGGER.info("[商品服务]-根据商品ID: {} 查询到商品信息: [{}].", id , GsonUtils.toJson(goods));
        return goods;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Goods goods) {
        try {
            int i = goodsMapper.insert(goods);
            if (i <= 0){
                LOGGER.info("[商品服务]-插入商品数据失败，回滚数据", GsonUtils.toJson(goods));
                throw new RuntimeException("[商品服务]-插入商品数据失败，回滚数据");
            }
        } catch (Exception e){
            LOGGER.info("[商品服务]-插入商品数据异常，回滚数据", GsonUtils.toJson(goods));
            throw new RuntimeException("[商品服务]-插入商品数据异常，回滚数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(Goods goods) {
        try {
            int i = goodsMapper.insert(goods);
            if (i <= 0){
                LOGGER.info("[商品服务]-修改商品数据失败，回滚数据", GsonUtils.toJson(goods));
                throw new RuntimeException("[商品服务]-修改商品数据失败，回滚数据");
            }
        } catch (Exception e){
            LOGGER.info("[商品服务]-修改商品数据异常，回滚数据", GsonUtils.toJson(goods));
            throw new RuntimeException("[商品服务]-修改商品数据异常，回滚数据");
        }
    }

    @Override
    public void deleteById(Long id) {
        LOGGER.info("[商品服务]-删除商品数据,商品ID: {}", id);
        goodsMapper.deleteById(id);
    }
}
