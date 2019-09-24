package org.igetwell.common.uitls;

public class StringUtils {

    public static String firstCharToLower(String rawString) {
        return prefixToLower(rawString, 1);
    }

    public static String prefixToLower(String rawString, int index) {
        String beforeChar = rawString.substring(0, index).toLowerCase();
        String afterChar = rawString.substring(index);
        return beforeChar + afterChar;
    }
}
