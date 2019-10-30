package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.common.uitls.GsonUtils;

/**
 * 折扣券
 */
@Getter
@Setter
public class DiscountCard extends AbstractCard {

    private Discount discount;

    public static void main(String[] args) {
        Discount discount = new Discount();
        WxCardBasis basis = new WxCardBasis();
        WxCardHigh high = new WxCardHigh();
        discount.setBaseInfo(basis);
        discount.setAdvancedInfo(high);
        discount.setDiscount(30);

        DiscountCard card = new DiscountCard();
        card.setCardType("DISCOUNT");
        card.setDiscount(discount);

        System.err.println(GsonUtils.toJson(card));
    }
}
