package com.major.extra.util;

public class StringUtil{

    public static boolean isEmpty(String value){
        return value == null || "".equalsIgnoreCase(value.trim());
    }

    public static boolean isNotEmpty(String value){
        return !isEmpty(value);
    }

}
