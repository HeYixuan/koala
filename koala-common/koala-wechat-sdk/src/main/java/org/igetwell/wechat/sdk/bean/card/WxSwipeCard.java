package org.igetwell.wechat.sdk.bean.card;

import java.io.Serializable;

public class WxSwipeCard implements Serializable {

    /**
     * 是否支持点击微信支付弹出付款码界面
     */
    private Boolean isSwipeCard;

    public Boolean getSwipeCard() {
        return isSwipeCard;
    }

    public void setSwipeCard(Boolean swipeCard) {
        isSwipeCard = swipeCard;
    }
}
