package org.igetwell.wechat.app.service;

import org.igetwell.wechat.sdk.bean.card.code.consume.CodeConsume;
import org.igetwell.wechat.sdk.bean.card.code.consume.CodeConsumeResponse;
import org.igetwell.wechat.sdk.bean.card.stock.CardStock;
import org.igetwell.wechat.sdk.bean.card.white.White;

/**
 * 微信会员卡
 */
public interface IWxCardService {

    /**
     * 创建投放卡券二维码
     * @param cardId
     * @param code
     * @return
     * @throws Exception
     */
    String launch(String cardId, String code) throws Exception;

    /**
     * 线下核销卡券Code
     */
    CodeConsumeResponse consume(CodeConsume consume) throws Exception;

    /**
     * 设置测试白名单
     */
    boolean white(White white) throws Exception;

    /**
     * 设置支持微信买单
     */
    boolean supplyPay(String cardId) throws Exception;

    /**
     * 设置自助核销
     */
    boolean selfConsume(String cardId) throws Exception;

    /**
     * 修改库存
     * @return
     */
    boolean stock(CardStock stock) throws Exception;

}
