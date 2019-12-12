package org.igetwell.wechat.sdk.bean.card;

import java.io.Serializable;

/**
 * 支付功能结构体,是否支持点击微信支付弹出付款码界面swipe_card结构
 */
public class WxCardSupplyPay implements Serializable {

    /**
     * 刷卡功能结构体,包含is_swipe_card字段
     */
    private WxSwipeCard swipeCard;

    public WxSwipeCard getSwipeCard() {
        return swipeCard;
    }

    public void setSwipeCard(WxSwipeCard swipeCard) {
        this.swipeCard = swipeCard;
    }
}
