package org.igetwell.wechat.app.service;

import org.igetwell.wechat.sdk.bean.card.*;

public interface IWxCouponService {

    /**
     * 创建会员卡
     */
    String createCard(WxMemberCard wxMemberCard) throws Exception;

    /**
     * 创建团购券
     */
    String createGroup(Group group) throws Exception;

    /**
     * 创建代金券
     */
    String createCash(Cash cash) throws Exception;

    /**
     * 创建兑换券
     */
    String createGift(Gift gift) throws Exception;

    /**
     * 创建折扣券
     */
    String createDiscount(Discount discount) throws Exception;

    /**
     * 创建优惠券
     */
    String createCoupon(Coupon coupon) throws Exception;

    /**
     * 统一创建卡券
     * @param create json字符串
     */
    String create(String create) throws Exception;
}
