package org.igetwell.common.uitls;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {

    /**
     * 小数的精度
     */
    private static int scale = 2;

    /* 两数相加
     * @param b1 操作数1
     * @param b2 操作数2
     * @return 结果
     */
    public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.add(b2);
    }

    /**
     * 两数相减
     * @param b1 操作数1
     * @param b2 操作数2
     * @return 结果
     */
    public static BigDecimal subtract(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.subtract(b2);
    }

    /**
     * 两数相乘
     * @param b1 操作数1
     * @param b2 操作数2
     * @return 结果
     */
    public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.multiply(b2).setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * 除法，默认留2位小数
     * @param b1 操作数1
     * @param b2 操作数2
     * @return
     */
    public static BigDecimal divide(BigDecimal b1, BigDecimal b2) {
        if(b1 == null) {
            return new BigDecimal(0);
        }
        if(b2  == null || equals(b2,new BigDecimal(0))){
            throw new ArithmeticException("b2 is zero ! ");
        }
        return b1.divide(b2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 除法（需要给定小数位数）
     * @param b1 操作数1
     * @param b2 操作数2
     * @return
     */
    public static BigDecimal divide(BigDecimal b1, BigDecimal b2, int scale) {
        if(b1 == null) {
            return new BigDecimal(0);
        }
        if(b2  == null || equals(b2,new BigDecimal(0))){
            throw new ArithmeticException("b2 is zero ! ");
        }
        return b1.divide(b2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 两数相乘
     * @param b1 操作数1
     * @param b2 操作数2
     * @return 结果
     */
    public static BigDecimal multiply(BigDecimal b1, BigDecimal b2, int scale) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.multiply(b2).setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * 大于
     * @param b1 操作数1
     * @param b2 操作数2
     * @return 结果
     */
    public static boolean greaterThan(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.compareTo(b2)>0;
    }

    /**
     * 大于或等于
     * @param b1 操作数1
     * @param b2 操作数2
     * @return 结果
     */
    public static boolean greaterOrEquals(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.compareTo(b2)>=0;
    }

    /**
     * 小于
     * @param b1 操作数1
     * @param b2 操作数2
     * @return 结果
     */
    public static boolean lessThan(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.compareTo(b2) < 0;
    }

    /**
     * 小于或等于
     * @param b1 操作数1
     * @param b2 操作数2
     * @return 结果
     */
    public static boolean lessOrEquals(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.compareTo(b2) <= 0;
    }

    /**
     * 等于
     * @param b1 操作数1
     * @param b2 操作数2
     * @return 结果
     */
    public static boolean equals(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.compareTo(b2) == 0;
    }


    public static void main(String[] args) {
        BigDecimal a1 = new BigDecimal(1.745);
        BigDecimal a2 = new BigDecimal(0.745);
        System.err.println(multiply(a1, a2));
    }
}
