package org.igetwell.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivateType {

    /**
     * 自动激活
     */
    AUTO(1, "AUTO_ACTIVATE"),

    /**
     * 一键激活
     */
    WX_ACTIVATE(2, "WX_ACTIVATE"),

    /**
     * 自定义激活会员卡的URL
     */
    CUSTOM_ACTIVATE(3, "ACTIVATE_URL");

    /**
     * 类型
     */
    private int value;
    /**
     * 类型
     */
    private String text;
}
