package org.igetwell.common.uitls;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CharacterUtils extends StringUtils {

    public static String firstCharToLower(String rawString) {
        return prefixToLower(rawString, 1);
    }

    public static String prefixToLower(String rawString, int index) {
        String beforeChar = rawString.substring(0, index).toLowerCase();
        String afterChar = rawString.substring(index);
        return beforeChar + afterChar;
    }

    public static List<String> toArrayList(String string, String split){
        if (isNotBlank(string)) {
            return Arrays.asList(string.split(split));
        }
        return new ArrayList<>();
    }

    public static String[] toArray(String string, String split){
        if (isNotBlank(string)) {
            return string.split(split);
        }
        return new String[string.split(split).length-1];
    }

    public static Integer[] toArray(String [] arr){
        Integer[] integers = new Integer[arr.length];
        for(int i=0;i<arr.length;i++){
            integers[i] = Integer.parseInt(arr[i]);
        }
        return integers;
    }

    public static void main(String[] args) {
        String s = "";
        String [] arr = toArray(s, ",");
        System.err.println(GsonUtils.toJson(arr));

        Integer integer [] = toArray(arr);
        System.err.println(GsonUtils.toJson(integer));

        List<String> list = toArrayList(s, ",");
        //System.err.println(list);
    }
}
