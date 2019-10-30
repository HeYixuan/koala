package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;

/**
 * 优惠券
 */
@Getter
@Setter
public class Coupon extends AbstractInfo {

    /**
     * 优惠券专用，填写优惠详情
     * 添加必填，不支持修改
     */
    private String defaultDetail;
}
