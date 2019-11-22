package org.igetwell.system.member.service;


import org.igetwell.order.entity.SeckillProduct;

import java.util.List;

public interface ISecKillProductService {

    /**
     * 获取秒杀商品列表
     * @return
     */
    List<SeckillProduct> getList();

    /**
     * 从缓存获取商品信息
     * @param id
     * @return
     */
    SeckillProduct getCache(String id);

    /**
     * 从数据库获取商品信息
     * @param id
     * @return
     */
    SeckillProduct get(String id);

    /**
     * 减库存,基于缓存（虚拟）
     * @param prodId
     * @return
     */
    boolean reduceStockCache(String prodId);

    /**
     * 减库存,基于乐观锁
     * @param prodId
     * @return
     */
    boolean reduceStock(String prodId);

}
