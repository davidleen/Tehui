package com.davidleen29.tehui.utils;

/**
 * 字符串的功能类。
 */

public class StringUtils {



    public static boolean  isEmpty(String s)
    {
        return s==null||s.trim().length()==0;
    }

    public static final String row_separator="\n";
    public static final String spec_separator="*";

    public static final String spec_separator_pattern="\\*";
    public static final float CM_TO_INCH=0.3937f;
    public static final float INCH_TO_CM=1/CM_TO_INCH;




    /**
     * 将数字用* 串联起来
     * @param value
     * @return
     */
    public static String combineNumberValue(Number... value)
    {

        String result="";

        int length = value.length;
        for (int i = 0; i < length; i++) {

            result+=value[i];
            if(i<length-1) {

                result+= spec_separator;
            }
        }

        return result;

    }


    public static boolean isDigitalString(String s) {


        for (int i = 0; i < s.length(); i++) {
            char ch=s.charAt(i);
            if(ch<48||ch>57)
                return false;
        }

        return true;
    }
}
