package org.igetwell.common.uitls;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.FieldPosition;

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
            b1 = BigDecimal.ZERO;
        }
        if (b2 == null) {
            b2 = BigDecimal.ZERO;
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
            b1 = BigDecimal.ZERO;
        }
        if (b2 == null) {
            b2 = BigDecimal.ZERO;
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
            b1 = BigDecimal.ZERO;
        }
        if (b2 == null) {
            b2 = BigDecimal.ZERO;
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
            return BigDecimal.ZERO;
        }
        if(b2  == null || equals(b2, BigDecimal.ZERO)){
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
            return BigDecimal.ZERO;
        }
        if(b2  == null || equals(b2, BigDecimal.ZERO)){
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
            b1 = BigDecimal.ZERO;
        }
        if (b2 == null) {
            b2 = BigDecimal.ZERO;
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
            b1 = BigDecimal.ZERO;
        }
        if (b2 == null) {
            b2 = BigDecimal.ZERO;
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
            b1 = BigDecimal.ZERO;
        }
        if (b2 == null) {
            b2 = BigDecimal.ZERO;
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
            b1 = BigDecimal.ZERO;
        }
        if (b2 == null) {
            b2 = BigDecimal.ZERO;
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
            b1 = BigDecimal.ZERO;
        }
        if (b2 == null) {
            b2 = BigDecimal.ZERO;
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
            b1 = BigDecimal.ZERO;
        }
        if (b2 == null) {
            b2 = BigDecimal.ZERO;
        }
        return b1.compareTo(b2) == 0;
    }

    /**
     * 将字符串"元"转换成"分"
     * @param str
     * @return
     */
    public static String convertDollar2Cent(String str) {
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer sb = df.format(Double.parseDouble(str),
                new StringBuffer(), new FieldPosition(0));
        int idx = sb.toString().indexOf(".");
        sb.deleteCharAt(idx);
        for (; sb.length() != 1;) {
            if(sb.charAt(0) == '0') {
                sb.deleteCharAt(0);
            } else {
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 将字符串"分"转换成"元"（长格式），如：100分被转换为1.00元。
     * @param s
     * @return
     */
    public static String convertCent2Dollar(String s) {
        if("".equals(s) || s ==null){
            return "";
        }
        long l;
        if(s.length() != 0) {
            if(s.charAt(0) == '+') {
                s = s.substring(1);
            }
            l = Long.parseLong(s);
        } else {
            return "";
        }
        boolean negative = false;
        if(l < 0) {
            negative = true;
            l = Math.abs(l);
        }
        s = Long.toString(l);
        if(s.length() == 1)
            return(negative ? ("-0.0" + s) : ("0.0" + s));
        if(s.length() == 2)
            return(negative ? ("-0." + s) : ("0." + s));
        else
            return(negative ? ("-" + s.substring(0, s.length() - 2) + "." + s
                    .substring(s.length() - 2)) : (s.substring(0,
                    s.length() - 2)
                    + "." + s.substring(s.length() - 2)));
    }

    /**
     * 将字符串"分"转换成"元"（短格式），如：100分被转换为1元。
     * @param s
     * @return
     */
    public static String convertCent2DollarShort(String s) {
        String ss = convertCent2Dollar(s);
        ss = "" + Double.parseDouble(ss);
        if(ss.endsWith(".0"))
            return ss.substring(0, ss.length() - 2);
        if(ss.endsWith(".00"))
            return ss.substring(0, ss.length() - 3);
        else
            return ss;
    }



    public static void main(String[] args) {
        BigDecimal a1 = new BigDecimal(1.745);
        BigDecimal a2 = new BigDecimal(0.745);
        System.err.println(multiply(a1, a2));
    }
}
