package org.igetwell.common.enums;


/**
 * 订单类型
 */
public enum OrderType {

    CHARGE(1, "充值"),
    CONSUME(2, "消费"),
    OPEN(3, "开卡");

    private final int value;

    private final String message;

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    private OrderType(int value, String message) {
        this.value = value;
        this.message = message;
    }





}
