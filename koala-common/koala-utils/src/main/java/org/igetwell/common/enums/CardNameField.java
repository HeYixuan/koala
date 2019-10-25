package org.igetwell.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardNameField {

    /**
     * 等级
     */
    FIELD_NAME_TYPE_LEVEL("等级", "FIELD_NAME_TYPE_LEVEL"),

    /**
     * 优惠券
     */
    FIELD_NAME_TYPE_COUPON("优惠券", "FIELD_NAME_TYPE_COUPON"),

    /**
     * 印花
     */
    FIELD_NAME_TYPE_STAMP("印花", "FIELD_NAME_TYPE_STAMP"),

    /**
     * 折扣
     */
    FIELD_NAME_TYPE_DISCOUNT("折扣", "FIELD_NAME_TYPE_DISCOUNT"),

    /**
     * 成就
     */
    FIELD_NAME_TYPE_ACHIEVEMEN("成就", "FIELD_NAME_TYPE_ACHIEVEMEN"),

    /**
     * 里程
     */
    FIELD_NAME_TYPE_MILEAGE("里程", "FIELD_NAME_TYPE_MILEAGE"),

    /**
     * 集点
     */
    FIELD_NAME_TYPE_SET_POINTS("集点", "FIELD_NAME_TYPE_SET_POINTS"),

    /**
     * 次数
     */
    FIELD_NAME_TYPE_TIMS("次数", "FIELD_NAME_TYPE_TIMS");


    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;

}
