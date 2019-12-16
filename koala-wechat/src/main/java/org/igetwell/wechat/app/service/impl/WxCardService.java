package org.igetwell.wechat.app.service.impl;

import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.wechat.app.service.IWxAppService;
import org.igetwell.wechat.app.service.IWxCardService;
import org.igetwell.wechat.sdk.api.CardAPI;
import org.igetwell.wechat.sdk.bean.card.code.consume.CodeConsume;
import org.igetwell.wechat.sdk.bean.card.code.consume.CodeConsumeResponse;
import org.igetwell.wechat.sdk.bean.card.code.consume.SelfConsume;
import org.igetwell.wechat.sdk.bean.card.code.consume.SupplyPay;
import org.igetwell.wechat.sdk.bean.card.qrcode.ActionCard;
import org.igetwell.wechat.sdk.bean.card.qrcode.ActionInfo;
import org.igetwell.wechat.sdk.bean.card.qrcode.QrCodeCreate;
import org.igetwell.wechat.sdk.bean.card.qrcode.QrCodeCreateResponse;
import org.igetwell.wechat.sdk.bean.card.stock.CardStock;
import org.igetwell.wechat.sdk.bean.card.white.White;
import org.igetwell.wechat.sdk.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class WxCardService implements IWxCardService {

    private static Logger logger = LoggerFactory.getLogger(WxCardService.class);

    @Autowired
    private IWxAppService iWxAppService;


    /**
     * 创建投放卡券二维码
     * @param cardId
     * @param code
     * @return
     * @throws Exception
     */
    public String launch(String cardId, String code) throws Exception {
        logger.info("[微信会员卡服务]-创建投放卡券二维码开始. {}, {}", cardId, code);
        QrCodeCreate create = new QrCodeCreate();
        create.setActionName("QR_CARD");
        if (StringUtils.isEmpty(cardId)) {
            logger.error("[微信会员卡服务]-创建投放卡券二维码开始失败, cardId不可为空.");
            throw new IllegalArgumentException("[微信会员卡服务]-创建投放卡券二维码开始失败, cardId不可为空.");
        }
        ActionCard actionCard = new ActionCard();
        actionCard.setCardId(cardId);
        if (!StringUtils.isEmpty(cardId)) {
            actionCard.setCode(code);
        }
        ActionInfo actionInfo = new ActionInfo();
        actionInfo.setCard(actionCard);
        create.setActionInfo(actionInfo);

        QrCodeCreateResponse response = CardAPI.createQrCode(iWxAppService.getAccessToken(), GsonUtils.toJson(create));
        if (StringUtils.isEmpty(response) || StringUtils.isEmpty(response.getShowQrcodeUrl())) {
            logger.error("[微信会员卡服务]-创建投放卡券二维码失败.");
            throw new IllegalArgumentException("[微信会员卡服务]-创建投放卡券二维码失败.");
        }
        logger.info("[微信会员卡服务]-创建投放卡券二维码结束.");
        return response.getShowQrcodeUrl();
    }

    /**
     * 线下核销卡券Code
     */
    @Override
    public CodeConsumeResponse consume(CodeConsume consume) throws Exception {
        logger.info("[微信会员卡服务]-微信会员卡线下核销卡券Code开始. {}", GsonUtils.toJson(consume));
        if (StringUtils.isEmpty(consume) || StringUtils.isEmpty(consume.getCode())) {
            logger.error("[微信会员卡服务]-微信会员卡线下核销卡券Code失败.请求参数为空.");
            throw new IllegalArgumentException("[微信会员卡服务]-微信会员卡线下核销卡券Code失败.请求参数为空.");
        }
        CodeConsumeResponse consumeResponse = CardAPI.codeConsume(iWxAppService.getAccessToken(), consume);
        if (StringUtils.isEmpty(consumeResponse) || StringUtils.isEmpty(consumeResponse.getCard())
                        || StringUtils.isEmpty(consumeResponse.getOpenid())
                || StringUtils.isEmpty(consumeResponse.getCard().getCardId())) {
            throw new RuntimeException("[微信会员卡服务]-微信会员卡线下核销卡券Code失败.");
        }
        logger.info("[微信会员卡服务]-微信会员卡线下核销卡券Code卡结束. {}", GsonUtils.toJson(consumeResponse));
        return consumeResponse;
    }

    /**
     * 设置测试白名单
     * @param white
     */
    public boolean white(White white) throws Exception {
        logger.info("[微信会员卡服务]-微信会员卡设置测试白名单开始. {}", GsonUtils.toJson(white));
        BaseResponse response = CardAPI.white(iWxAppService.getAccessToken(), GsonUtils.toJson(white));
        if (!response.isSuccess()) {
            throw new RuntimeException("[微信会员卡服务]-微信会员卡设置测试白名单失败.");
        }
        logger.info("[微信会员卡服务]-微信会员卡设置测试白名单结束.");
        return response.isSuccess();
    }

    /**
     * 设置支持微信买单
     */
    public boolean supplyPay(String cardId) throws Exception {
        logger.info("[微信会员卡服务]-设置支持微信买单开始. {}", cardId);
        if (StringUtils.isEmpty(cardId)) {
            logger.error("[微信会员卡服务]-设置支持微信买单失败.请求参数为空.");
            throw new IllegalArgumentException("[微信会员卡服务]-设置支持微信买单失败.请求参数为空.");
        }
        SupplyPay supplyPay = new SupplyPay();
        supplyPay.setCardId(cardId);
        BaseResponse response = CardAPI.supplyPay(iWxAppService.getAccessToken(), supplyPay);
        if (!response.isSuccess()) {
            throw new RuntimeException("[微信会员卡服务]-设置支持微信买单失败.");
        }
        logger.info("[微信会员卡服务]-设置支持微信买单结束.");
        return true;
    }

    /**
     * 设置自助核销
     */
    public boolean selfConsume(String cardId) throws Exception {
        logger.info("[微信会员卡服务]-设置自助核销开始. {}", cardId);
        if (StringUtils.isEmpty(cardId)) {
            logger.error("[微信会员卡服务]-设置自助核销失败.请求参数为空.");
            throw new IllegalArgumentException("[微信会员卡服务]-设置自助核销失败.请求参数为空.");
        }
        SelfConsume consume = new SelfConsume();
        consume.setCardId(cardId);
        BaseResponse response = CardAPI.selfConsume(iWxAppService.getAccessToken(), consume);
        if (!response.isSuccess()) {
            throw new RuntimeException("[微信会员卡服务]-设置自助核销失败.");
        }
        logger.info("[微信会员卡服务]-设置自助核销结束.");
        return true;
    }

    /**
     * 修改库存
     * @return
     */
    public boolean stock(CardStock stock) throws Exception {
        logger.info("[微信会员卡服务]-修改库存开始. {}", GsonUtils.toJson(stock));
        if (StringUtils.isEmpty(stock) || StringUtils.isEmpty(stock.getCardId()) || StringUtils.isEmpty(stock.getIncreaseStockValue())) {
            logger.error("[微信会员卡服务]-修改库存失败.请求参数为空.");
            throw new IllegalArgumentException("[微信会员卡服务]-修改库存失败.请求参数为空.");
        }
        BaseResponse response = CardAPI.stock(iWxAppService.getAccessToken(), stock);
        if (!response.isSuccess()) {
            throw new RuntimeException("[微信会员卡服务]-修改库存失败.");
        }
        logger.info("[微信会员卡服务]-修改库存结束.");
        return true;
    }
}
