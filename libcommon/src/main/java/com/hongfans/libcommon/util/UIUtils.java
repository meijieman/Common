package com.hongfans.libcommon.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import com.hongfans.libcommon.BaseApp;


/**
 * ui相关的一些静态工具方法
 */
public class UIUtils{

    /** 得到 string.xml 中的一个字符串 */
    public static String getString(int resId){
        return getResources().getString(resId);
    }

    /** 得到 resource 对象 */
    public static Resources getResources(){
        return getContext().getResources();
    }

    /** 得到上下文 */
    public static Context getContext(){
        return BaseApp.getInstance();
    }

    /** 得到 string.xml 中的一个字符串数组 */
    public static String[] getStringArr(int resId){
        return getResources().getStringArray(resId);
    }

    /** 得到 color.xml 中的颜色值 */
    public static int getColor(int colorId){
        return getResources().getColor(colorId);
    }

    /** 得到应用程序的包名 */
    public static String getPackageName(){
        return getContext().getPackageName();
    }

    /** 安全的执行一个task */
    public static void postTaskSafely(Runnable task){
        long curThreadId = android.os.Process.myTid();
        long mainThreadId = getMainThreadId();
        // 如果当前线程是主线程
        if(curThreadId == mainThreadId){
            task.run();
        } else {
            // 如果当前线程不是主线程
            getMainThreadHandler().post(task);
        }
    }

    /** 得到主线程id */
    public static long getMainThreadId(){
        return BaseApp.getMainThreadId();
    }

    /** 得到一个主线程的handler */
    public static Handler getMainThreadHandler(){
        return BaseApp.getHandler();
    }
}
