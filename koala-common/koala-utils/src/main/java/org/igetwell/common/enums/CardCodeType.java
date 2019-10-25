package org.igetwell.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 卡展示方式
 */
@Getter
@AllArgsConstructor
public enum CardCodeType {
    /**
     *
     * "CODE_TYPE_TEXT"，文本；<br>
     * "CODE_TYPE_BARCODE"，一维码； <br>
     * "CODE_TYPE_QRCODE"，二维码；<br>
     * "CODE_TYPE_ONLY_QRCODE"，二维码无code显示；<br>
     * "CODE_TYPE_ONLY_BARCODE"，一维码无code显示；<br>
     * "CODE_TYPE_NONE"，不显示code和条形码类型<br>
     * 添加必填
     */

    /**
     * 条形码
     */
    BARCODE(1, "CODE_TYPE_BARCODE"),

    /**
     * 二维码
     */
    QRCODE(2, "CODE_TYPE_QRCODE"),

    /**
     * 文本
     */
    TEXT(3, "CODE_TYPE_TEXT"),

    /**
     * 二维码无code显示
     */
    ONLY_QRCODE(4, "CODE_TYPE_ONLY_QRCODE"),

    /**
     * 条形码无code显示
     */
    ONLY_BARCODE(5, "CODE_TYPE_ONLY_BARCODE"),

    /**
     * 不显示code和条形码类型
     */
    NONE(6, "CODE_TYPE_NONE");

    /**
     * 类型
     */
    private int type;
    /**
     * 描述
     */
    private String value;
}
