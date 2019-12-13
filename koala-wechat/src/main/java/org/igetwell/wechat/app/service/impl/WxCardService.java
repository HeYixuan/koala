package org.igetwell.wechat.app.service.impl;

import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.wechat.app.service.IWxAppService;
import org.igetwell.wechat.app.service.IWxCardService;
import org.igetwell.wechat.sdk.api.CardAPI;
import org.igetwell.wechat.sdk.bean.card.MemberCard;
import org.igetwell.wechat.sdk.bean.card.code.consume.CodeConsume;
import org.igetwell.wechat.sdk.bean.card.code.consume.CodeConsumeResponse;
import org.igetwell.wechat.sdk.bean.card.create.WxCardCreate;
import org.igetwell.wechat.sdk.bean.card.qrcode.ActionCard;
import org.igetwell.wechat.sdk.bean.card.qrcode.ActionInfo;
import org.igetwell.wechat.sdk.bean.card.qrcode.QrCodeCreate;
import org.igetwell.wechat.sdk.bean.card.qrcode.QrCodeCreateResponse;
import org.igetwell.wechat.sdk.bean.card.white.White;
import org.igetwell.wechat.sdk.response.BaseResponse;
import org.igetwell.wechat.sdk.response.Card;
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
     * 创建微信会员卡
     * @param create
     * @return
     * @throws Exception
     */
    @Override
    public String createCard(WxCardCreate<MemberCard> create) throws Exception {
        logger.info("[微信会员卡服务]-创建微信会员卡开始. {}", GsonUtils.toJson(create));
        if (StringUtils.isEmpty(create) || StringUtils.isEmpty(create.getCard()) || StringUtils.isEmpty(create.getCard().getMemberCard())) {
            logger.error("[微信会员卡服务]-创建微信会员卡失败, 请求参数为空.");
            throw new IllegalArgumentException("[微信会员卡服务]-创建微信会员卡失败, 请求参数错误.");
        }
        Card card = CardAPI.create(iWxAppService.getAccessToken(), create);
        if (StringUtils.isEmpty(card) || StringUtils.isEmpty(card.getCardId())) {
            logger.error("[微信会员卡服务]-创建微信会员卡失败.");
            throw new RuntimeException("[微信会员卡服务]-创建微信会员卡失败.");
        }
        logger.info("[微信会员卡服务]-创建微信会员卡结束. {}", card.getCardId());
        return card.getCardId();
    }

    /**
     * 创建微信会员卡
     */
    public String createCard(String create) throws Exception {
        logger.info("[微信会员卡服务]-创建微信会员卡开始. {}", create);
        String cardId = createCoupon(create);
        logger.info("[微信会员卡服务]-创建微信会员卡结束. {}", cardId);
        return cardId;
    }

    /**
     * 创建团购券
     */
    public String createGroup(String group) throws Exception {
        logger.info("[微信会员卡服务]-创建微信团购券开始. {}", group);
        String cardId = createCoupon(group);
        logger.info("[微信会员卡服务]-创建微信团购券结束. {}", cardId);
        return cardId;
    }


    /**
     * 创建代金券
     * @param cash
     * @return
     */
    public String createCash(String cash) throws Exception {
        logger.info("[微信会员卡服务]-创建微信代金券开始. {}", cash);
        String cardId = createCoupon(cash);
        logger.info("[微信会员卡服务]-创建微信代金券结束. {}", cardId);
        return cardId;
    }

    /**
     * 创建折扣券
     * @param discount
     * @return
     */
    public String createDiscount(String discount) throws Exception {
        logger.info("[微信会员卡服务]-创建微信折扣券开始. {}", discount);
        String cardId = createCoupon(discount);
        logger.info("[微信会员卡服务]-创建微信折扣券结束. {}", cardId);
        return cardId;
    }

    /**
     * 创建兑换券
     * @param gift
     * @return
     * @throws Exception
     */
    public String createGift(String gift) throws Exception {
        logger.info("[微信会员卡服务]-创建微信兑换券开始. {}", gift);
        String cardId = createCoupon(gift);
        logger.info("[微信会员卡服务]-创建微信兑换券结束. {}", cardId);
        return cardId;
    }

    /**
     * 创建卡券
     * @param coupon
     * @return
     * @throws Exception
     */
    private String createCoupon(String coupon) throws Exception {
        if (StringUtils.isEmpty(coupon) || StringUtils.isEmpty(coupon.trim())) {
            logger.error("[微信会员卡服务]-创建微信卡券失败, 请求参数为空.");
            throw new IllegalArgumentException("[微信会员卡服务]-创建微信卡券失败, 请求参数错误.");
        }
        Card card = CardAPI.create(iWxAppService.getAccessToken(), coupon);
        if (StringUtils.isEmpty(card) || StringUtils.isEmpty(card.getCardId())) {
            logger.error("[微信会员卡服务]-创建微信卡券失败.");
            throw new RuntimeException("[微信会员卡服务]-创建微信卡券失败.");
        }
        return card.getCardId();
    }


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
        logger.info("[微信会员卡服务]-微信会员卡设置测试白名单结束.");
        return response.isSuccess();
    }
}
