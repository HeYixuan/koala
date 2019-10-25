package org.igetwell.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardButtonType {

    MEMBER_PAY(1, "会员支付"),
    WECHAT_PAY(2, "微信支付"),
    CUSTOM_PAY(3, "自定义"),
    NO_DISPLAY(4, "不展示");

    /**
     * 类型
     */
    private int value;
    /**
     * 类型
     */
    private String text;
}
