package com.hongfans.libcommon.log;

import android.util.Log;

/**
 * @desc: 简化版 log 输出
 * @author: Major
 * @since: 2017/8/13 19:56
 */
public class SimpleLog{

    private static final String TAG = "SimpleLog";

    public static void i(String msg){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        StackTraceElement element = stackTrace[3];
        String pkgName = getPkgName(element.getClassName()); // 获取包名
        sb.append(element.toString().replace(pkgName + ".", ""));
        sb.append(": ");
        sb.append(msg);

        Log.i(TAG, sb.toString());
    }

    private static String getPkgName(String className){
        if(className.contains("$")){
            // 内部类
            className = className.substring(0, className.indexOf("$"));
        }
        className = className.substring(0, className.lastIndexOf("."));

        return className;
    }
}
