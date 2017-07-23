package com.hongfans.libcommon.log;

import android.util.Log;

/**
 * @desc: TODO
 * @author: Major
 * @since: 2017/6/26 23:06
 */
public class LogUtil{

    private static final int LEVEL_V = 0;
    private static final int LEVEL_I = 1;
    private static final int LEVEL_W = 2;
    private static final int LEVEL_E = 3;
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
        e("", msg);
    }

    public static void e(String tag, String msg){
        if(mIsDebug){
            String trace = "";
            if(mIsTrack){
                trace = getTrace();
            }
            log(tag, trace + msg, LEVEL_E);
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

    private static void log(String tag, String msg, int level){
        switch(level) {
            case LEVEL_V:
                Log.v(mTag + tag, msg);
                break;
            case LEVEL_I:
                Log.i(mTag + tag, msg);
                break;
            case LEVEL_W:
                Log.w(mTag + tag, msg);
                break;
            case LEVEL_E:
                Log.e(mTag + tag, msg);
                break;
        }
    }
}
