package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.common.uitls.GsonUtils;

/**
 * 优惠券
 */
@Getter
@Setter
public class CouponCard extends AbstractCard {

    private Coupon generalCoupon;

    public static void main(String[] args) {
        Coupon coupon = new Coupon();
        WxCardBasis basis = new WxCardBasis();
        WxCardHigh high = new WxCardHigh();
        coupon.setBaseInfo(basis);
        coupon.setAdvancedInfo(high);
        coupon.setDefaultDetail("优惠券专用，填写优惠详情");

        CouponCard card = new CouponCard();
        card.setCardType("GENERAL_COUPON");
        card.setGeneralCoupon(coupon);

        System.err.println(GsonUtils.toJson(card));
    }
}
