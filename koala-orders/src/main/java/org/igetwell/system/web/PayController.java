package org.igetwell.system.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.dto.request.ChargeOrderRequest;
import org.igetwell.system.order.dto.request.OrderPayRequest;
import org.igetwell.system.goods.entity.Goods;
import org.igetwell.system.goods.service.IGoodsService;
import org.igetwell.system.order.service.IWxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

/**
 * 支付
 */
@RestController
public class PayController {

    @Autowired
    private IWxPayService iWxPayService;
    @Autowired
    private IGoodsService iGoodsService;


    @PostMapping("/api/wxPay")
    public ResponseEntity wxPay(@RequestBody OrderPayRequest request){
        return iWxPayService.createOrder(request);
    }

    @PostMapping("/api/getGoods")
    public ResponseEntity getGoods(){
        List<Goods> goods = iGoodsService.getList();
        return ResponseEntity.ok(goods);
    }

    @PostMapping("/api/getGoods/{id}")
    public ResponseEntity getGoods(@PathVariable("id") Long id){
        Goods goods = iGoodsService.getCache(id);
        return ResponseEntity.ok(goods);
    }

    @PostMapping("/api/goods/createOrder")
    public ResponseEntity createOrder(@RequestBody ChargeOrderRequest chargeOrderRequest){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String sessionId = attributes.getSessionId();
        ResponseEntity responseEntity = iGoodsService.createOrder(chargeOrderRequest);
        return responseEntity;
    }

}
