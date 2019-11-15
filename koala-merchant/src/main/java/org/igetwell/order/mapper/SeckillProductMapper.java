package org.igetwell.order.mapper;

import org.igetwell.order.entity.SeckillProduct;

import java.util.List;

/**
 * 秒杀商品Mapper接口
 */
public interface SeckillProductMapper {

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
    SeckillProduct get(Integer id);

    void delete(Integer id);

    int insert(SeckillProduct product);

    /**
     * 更新商品信息
     * @param product
     * @return
     */
    int update(SeckillProduct product);

    /**
     * 商品扣减库存
     * @param product
     * @return
     */
    int reduceStock(SeckillProduct product);

}