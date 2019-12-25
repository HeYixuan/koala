package org.igetwell.wechat.app.service;


public interface IAlipayService {

    /**
     * 扫码预付款下单
     * @param productName
     * @param productId
     * @param fee
     * @return
     */
    String wapPay(String subject, String productName, String productId, String fee) throws Exception;
}
