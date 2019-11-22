package org.igetwell.system.order.service;

import org.igetwell.system.order.entity.Goods;

public interface IGoodsService {

    Goods get(Long id);

    Goods getCache(Long id);

    void update(Goods goods);

    void insert(Goods goods);

    void deleteById(Long id);
}
