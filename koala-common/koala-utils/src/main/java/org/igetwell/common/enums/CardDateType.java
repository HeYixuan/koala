package org.igetwell.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardDateType {
    /**
     * 永久有效
     */
    PERMANENT(1, "DATE_TYPE_PERMANENT"),

    /**
     * 固定范围日期有效
     */
    FIX_DATE(2, "DATE_TYPE_FIX_TIME_RANGE"),

    /**
     * 固定时长有效
     */
    FIX_LONG_TIME(3, "DATE_TYPE_FIX_TERM");

    private int value;

    private String type;
}
