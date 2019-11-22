package org.igetwell.system.order.mapper;

import org.igetwell.system.order.entity.Goods;

import java.util.List;

public interface GoodsMapper {

    Goods get(Long id);

    List<Goods> getList();

    int update(Goods goods);

    int insert(Goods goods);

    int deleteById(Long id);

}