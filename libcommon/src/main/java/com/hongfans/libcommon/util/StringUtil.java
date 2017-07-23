package com.hongfans.libcommon.util;

import android.content.Context;
import android.text.format.Formatter;

public class StringUtil{

    public static boolean isEmpty(String value){
        return value == null || "".equalsIgnoreCase(value.trim());
    }

    public static boolean isNotEmpty(String value){
        return !isEmpty(value);
    }

}
