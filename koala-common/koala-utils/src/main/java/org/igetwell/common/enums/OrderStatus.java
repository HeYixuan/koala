package org.igetwell.common.enums;

public enum OrderStatus {

    /** 支付 **/
    CREATE(0, "订单生成"),
    PENDING(1, "支付中"),
    PAID(2, "支付成功"),
    PAID_FAIL(3, "支付失败"),
    CANCEL(4, "用户取消支付"),
    CLOSE(5, "订单关闭"),
    REFUND(6, "已退款订单"),


    /** 退款 **/
    REFUNDING(1, "退款中"),
    REFUND_SUCCESS(2, "退款成功"),
    REFUND_FAIL(3, "退款失败"),
    REFUND_CLOSE(4, "退款关闭"),
    REFUND_REJECT(5, "商户拒绝退款");

    private int value;

    private String message;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    OrderStatus(int value, String message) {
        this.value = value;
        this.message = message;
    }
}
