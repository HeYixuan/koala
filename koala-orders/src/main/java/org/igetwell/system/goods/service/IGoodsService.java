package org.igetwell.system.goods.service;

import org.igetwell.system.goods.entity.Goods;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.dto.request.ChargeOrderRequest;

import java.util.List;

public interface IGoodsService {

    List<Goods> getList();

    Goods get(Long id);

    Goods getCache(Long id);

    void update(Goods goods);

    void insert(Goods goods);

    void deleteById(Long id);

    boolean reduceGoodsStock(Long goodsId);


    /**
     * 秒杀下单服务
     * @param request
     * @return
     */
    ResponseEntity createOrder(ChargeOrderRequest request);

    /**
     * 订单入队,缓存30分钟并生成订单记录,提示用户支付
     * @param request
     * @return
     */
    ResponseEntity createOrderQueue(ChargeOrderRequest request);
}
