package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.common.uitls.GsonUtils;

/**
 * 兑换券
 */
@Getter
@Setter
public class GiftCard extends AbstractCard {

    private Gift gift;

    public static void main(String[] args) {
        Gift gift = new Gift();
        WxCardBasis basis = new WxCardBasis();
        WxCardHigh high = new WxCardHigh();
        gift.setBaseInfo(basis);
        gift.setAdvancedInfo(high);
        gift.setGift("可兑换音乐木盒一个");

        GiftCard card = new GiftCard();
        card.setCardType("GIFT");
        card.setGift(gift);

        System.err.println(GsonUtils.toJson(card));
    }
}
