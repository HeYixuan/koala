package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WxSwipeCard implements Serializable {

    /**
     * 是否支持点击微信支付弹出付款码界面
     */
    private Boolean isSwipeCard;
}
