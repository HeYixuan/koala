package org.igetwell.union.service;

import java.util.Map;

public interface IUnionPayService {

    /**
     * 手机网站支付预付款下单
     */
    Map<String, String> wap(String tradeNo, String subject, String body, String fee);

    /**
     * PC网站支付
     */
    Map<String, String> web(String tradeNo, String subject, String body, String fee);

    /**
     * 扫码预付款下单(用户主扫)
     */
    Map<String, String> sca(String tradeNo, String productId, String body, String fee);

    /**
     * 扫码预付款下单(用户主扫)
     */
    Map<String, String> scan(String tradeNo, String productId, String body, String fee);
}
