package org.igetwell.system.order.mapper;


import org.igetwell.system.order.entity.TradeOrder;

public interface TradeOrderMapper {

    TradeOrder get(Long id);

    void deleteById(Long id);

    int insert(TradeOrder order);

    int update(TradeOrder order);
}