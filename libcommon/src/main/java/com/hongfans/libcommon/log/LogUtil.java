package com.hongfans.libcommon.log;

import android.util.Log;

/**
 * @desc: TODO
 * @author: Major
 * @since: 2017/6/26 23:06
 */
public class LogUtil{

    private static String mTag = "LogUtil";
    private static boolean mIsDebug;
    private static boolean mIsTrack;

    private LogUtil(){

    }

    public static void init(String tag, boolean isDebug, boolean isTrack){
        if(tag != null && tag.trim().length() != 0){
            mTag = tag;
        }
        mIsDebug = isDebug;
        mIsTrack = isTrack;
    }

    public static void e(String msg){
        if(mIsDebug){
            if(mIsTrack){
                getTrace();
            } else {

            }
        }
    }

    public static void e(String tag, String msg){
        if(mIsDebug){
            if(mIsTrack){
                getTrace();
            } else {

            }
        }
    }

    /**
     * @param tag * @param
     */
    /**
     * @param tag     设置的 tag
     * @param isTrack 根据当前的设置判断是否栈追踪
     * @param msg     需要打印的内容
     */
    public static void e(String tag, boolean isTrack, String msg){
        if(mIsDebug){
            if(isTrack){

            } else {

            }
        }
    }


    private static String getTrace(){
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for(int i = 0; i < elements.length; i++){
            String line = elements[i].getClassName().replace(".java", "")
                          + "." + elements[i].getMethodName()
                          + ":" + elements[i].getLineNumber();
            Log.i(mTag, "line " + line);
        }

        return null;

    }
}
