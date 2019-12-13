package org.igetwell.wechat.app.service.impl;

import org.igetwell.common.enums.CardType;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.wechat.app.service.IWxAppService;
import org.igetwell.wechat.app.service.IWxCouponService;
import org.igetwell.wechat.sdk.api.CardAPI;
import org.igetwell.wechat.sdk.bean.card.*;
import org.igetwell.wechat.sdk.bean.card.create.WxCardCreate;
import org.igetwell.wechat.sdk.response.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class WxCouponService implements IWxCouponService {

    private static Logger logger = LoggerFactory.getLogger(WxCouponService.class);

    @Autowired
    private IWxAppService iWxAppService;

    /**
     * 创建会员卡
     */
    public String createCard(WxMemberCard wxMemberCard) throws Exception {
        logger.info("[微信卡券服务]-创建微信会员卡开始. {}", GsonUtils.toJson(wxMemberCard));
        if (StringUtils.isEmpty(wxMemberCard) || StringUtils.isEmpty(wxMemberCard.getBaseInfo()) || StringUtils.isEmpty(wxMemberCard.getBaseInfo().getBrandName())) {
            throw new IllegalArgumentException("[微信卡券服务]-创建微信会员卡失败,请求参数为空.");
        }
        WxCardCreate<MemberCard> create = new WxCardCreate<MemberCard>();
        MemberCard memberCard = new MemberCard();
        memberCard.setCardType(CardType.MEMBER_CARD.name());
        memberCard.setMemberCard(wxMemberCard);
        create.setCard(memberCard);
        String cardId = createCoupon(create);
        logger.info("[微信卡券服务]-创建微信会员卡结束. {}", cardId);
        return cardId;
    }

    /**
     * 创建团购券
     */
    public String createGroup(Group group) throws Exception {
        logger.info("[微信卡券服务]-创建微信团购券开始. {}", GsonUtils.toJson(group));
        if (StringUtils.isEmpty(group) || StringUtils.isEmpty(group.getBaseInfo()) || StringUtils.isEmpty(group.getBaseInfo().getBrandName())) {
            throw new IllegalArgumentException("[微信卡券服务]-创建微信团购券失败,请求参数为空.");
        }
        WxCardCreate<GroupCard> create = new WxCardCreate<GroupCard>();
        GroupCard groupCard = new GroupCard();
        groupCard.setCardType(CardType.GROUPON.name());
        groupCard.setGroupon(group);
        create.setCard(groupCard);
        String cardId = createCoupon(create);
        logger.info("[微信卡券服务]-创建微信团购券结束. {}", cardId);
        return cardId;
    }

    /**
     * 创建代金券
     */
    public String createCash(Cash cash) throws Exception {
        logger.info("[微信卡券服务]-创建微信代金券开始. {}", GsonUtils.toJson(cash));
        if (StringUtils.isEmpty(cash) || StringUtils.isEmpty(cash.getBaseInfo()) || StringUtils.isEmpty(cash.getBaseInfo().getBrandName())) {
            throw new IllegalArgumentException("[微信卡券服务]-创建微信代金券失败,请求参数为空.");
        }
        WxCardCreate<CashCard> create = new WxCardCreate<CashCard>();
        CashCard cashCard = new CashCard();
        cashCard.setCardType(CardType.CASH.name());
        cashCard.setCash(cash);
        create.setCard(cashCard);
        String cardId = createCoupon(create);
        logger.info("[微信卡券服务]-创建微信代金券结束. {}", cardId);
        return cardId;
    }

    /**
     * 创建兑换券
     */
    public String createGift(Gift gift) throws Exception {
        logger.info("[微信卡券服务]-创建微信兑换券开始. {}", GsonUtils.toJson(gift));
        if (StringUtils.isEmpty(gift) || StringUtils.isEmpty(gift.getBaseInfo()) || StringUtils.isEmpty(gift.getBaseInfo().getBrandName())) {
            throw new IllegalArgumentException("[微信卡券服务]-创建微信兑换券失败,请求参数为空.");
        }
        WxCardCreate<GiftCard> create = new WxCardCreate<GiftCard>();
        GiftCard giftCard = new GiftCard();
        giftCard.setCardType(CardType.GIFT.name());
        giftCard.setGift(gift);
        create.setCard(giftCard);
        String cardId = createCoupon(create);
        logger.info("[微信卡券服务]-创建微信兑换券结束. {}", cardId);
        return cardId;
    }

    /**
     * 创建折扣券
     */
    public String createDiscount(Discount discount) throws Exception {
        logger.info("[微信卡券服务]-创建微信折扣券开始. {}", GsonUtils.toJson(discount));
        if (StringUtils.isEmpty(discount) || StringUtils.isEmpty(discount.getBaseInfo()) || StringUtils.isEmpty(discount.getBaseInfo().getBrandName())) {
            throw new IllegalArgumentException("[微信卡券服务]-创建微信折扣券失败,请求参数为空.");
        }
        WxCardCreate<DiscountCard> create = new WxCardCreate<DiscountCard>();
        DiscountCard discountCard = new DiscountCard();
        discountCard.setCardType(CardType.DISCOUNT.name());
        discountCard.setDiscount(discount);
        create.setCard(discountCard);
        String cardId = createCoupon(create);
        logger.info("[微信卡券服务]-创建微信折扣券结束. {}", cardId);
        return cardId;
    }

    /**
     * 创建优惠券
     */
    public String createCoupon(Coupon coupon) throws Exception {
        logger.info("[微信卡券服务]-创建微信优惠券开始. {}", GsonUtils.toJson(coupon));
        if (StringUtils.isEmpty(coupon) || StringUtils.isEmpty(coupon.getBaseInfo()) || StringUtils.isEmpty(coupon.getBaseInfo().getBrandName())) {
            throw new IllegalArgumentException("[微信卡券服务]-创建微信优惠券失败,请求参数为空.");
        }
        WxCardCreate<CouponCard> create = new WxCardCreate<CouponCard>();
        CouponCard cashCard = new CouponCard();
        cashCard.setCardType(CardType.GENERAL_COUPON.name());
        cashCard.setGeneralCoupon(coupon);
        create.setCard(cashCard);
        String cardId = createCoupon(create);
        logger.info("[微信卡券服务]-创建微信优惠券结束. {}", cardId);
        return cardId;
    }

    /**
     * 统一创建卡券
     * @param create json字符串
     */
    public String create(String create) throws Exception {
        logger.info("[微信卡券服务]-统一创建微信卡券开始. {}", create);
        if (StringUtils.isEmpty(create)) {
            logger.error("[微信卡券服务]-统一创建微信卡券失败.请求参数为空.");
            throw new IllegalArgumentException("[微信卡券服务]-统一创建微信卡券失败,请求参数为空.");
        }
        Card card = CardAPI.create(iWxAppService.getAccessToken(), create);
        if (StringUtils.isEmpty(card) || StringUtils.isEmpty(card.getCardId())) {
            logger.error("[微信卡券服务]-统一创建微信卡券失败.");
            throw new RuntimeException("[微信卡券服务]-统一创建微信卡券失败.");
        }
        return card.getCardId();
    }

    /**
     * 统一创建卡券
     */
    private String createCoupon(WxCardCreate<?> create) throws Exception {
        Card card = CardAPI.create(iWxAppService.getAccessToken(), create);
        if (StringUtils.isEmpty(card) || StringUtils.isEmpty(card.getCardId())) {
            logger.error("[微信卡券服务]-统一创建微信卡券失败.");
            throw new RuntimeException("[微信卡券服务]-统一创建微信卡券失败.");
        }
        return card.getCardId();
    }
}
