package org.igetwell.common.enums;

public enum OrderStatus {

    CREATE(0, "订单生成"),
    PENDING(1, "支付中"),
    PAID(2, "支付成功"),
    PAID_FAIL(3, "支付失败"),
    CANCEL(4, "用户取消支付"),
    REFUND(5, "已退款订单");

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
