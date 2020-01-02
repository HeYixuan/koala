package org.igetwell.common.enums;

/**
 * 交易类型
 */
public enum TradeType {

    /**
     * 原生扫码支付.
     */
    NATIVE,

    /**
     * App支付.
     */
    APP,

    /**
     * 公众号支付/小程序支付.
     */
    JSAPI,

    /**
     * H5支付.
     */
    MWEB,

    /**
     * 刷卡支付.
     * 刷卡支付有单独的支付接口，不调用统一下单接口
     */
    MICROPAY,


    /**
     * 支付宝-手机网站支付
     */
    WAP,

    /**
     * 支付宝-PC网站支付
     */
    PC,

    /**
     * 支付宝-面对面扫码支付
     */
    FACE_TO_FACE;

}
