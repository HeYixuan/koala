package org.igetwell.common.constans;

public class PayConstants {

    public final static String TRADE_WAIT = "WAIT_BUYER_PAY";		// 交易创建,等待买家付款
    public final static String TRADE_CLOSED = "TRADE_CLOSED";		// 交易关闭
    public final static String TRADE_SUCCESS = "TRADE_SUCCESS";		// 交易成功
    public final static String TRADE_FINISHED = "TRADE_FINISHED";	// 交易成功且结束
    public final static String REFUND_SUCCESS = "REFUND_SUCCESS";		// 退款成功
    public final static String REFUND_CLOSED = "REFUND_CLOSED";		// 退款成功

    //支付宝回调返回字符串
    public static final String FAIL     = "fail";
    public static final String SUCCESS  = "success";

    //银联回调返回字符串
    public static final String OK     = "ok";
    public static final String ERROR  = "error";
}
