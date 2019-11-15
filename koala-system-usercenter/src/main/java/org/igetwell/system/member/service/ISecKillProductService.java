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
     * 根据产品id查询产品
     * @param id
     * @return
     */
    SeckillProduct get(String id);

    /**
     * 减库存,基于乐观锁
     * @param prodId
     * @return
     */
    boolean reduceStock(String prodId);

}
