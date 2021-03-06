package com.major.base.log;

import android.util.Log;

/**
 * TODO
 * Created by MEI on 2017/9/8.
 */

public class LogUtil {

    public static final int LEVEL_V = 1;
    public static final int LEVEL_D = 2;
    public static final int LEVEL_I = 3;
    public static final int LEVEL_W = 4;
    public static final int LEVEL_E = 5;

    private static String mTag = "LogUtil_";

    private static boolean mIsDebug;
    private static boolean mIsTrack;
    private static String mPkgName;

    LogUtil() {
    }

    public static void init(boolean isDebug) {
        // isTrack 为 false 时可以将 pkgName 设置为 null
        init(null, null, isDebug, false);
    }

    public static void init(String pkgName, String tag, boolean isDebug, boolean isTrack) {
        if (tag != null && tag.trim().length() != 0) {
            mTag = tag;
        }
        mIsDebug = isDebug;
        mIsTrack = isTrack;
        mPkgName = pkgName;
    }

    public static void v(Object msg) {
        log(mIsDebug, mIsTrack, "", msg, LEVEL_V);
    }

    public static void v(String tag, Object msg) {
        log(mIsDebug, mIsTrack, tag, msg, LEVEL_V);
    }

    public static void d(Object msg) {
        log(mIsDebug, mIsTrack, "", msg, LEVEL_D);
    }

    public static void d(String tag, Object msg) {
        log(mIsDebug, mIsTrack, tag, msg, LEVEL_D);
    }

    public static void i(Object msg) {
        log(mIsDebug, mIsTrack, "", msg, LEVEL_I);
    }

    public static void i(String tag, Object msg) {
        log(mIsDebug, mIsTrack, tag, msg, LEVEL_I);
    }

    public static void i(String tag, Object msg, boolean isDebug) {
        if (isDebug) {
            log(mIsDebug, mIsTrack, tag, msg, LEVEL_I);
        }
    }

    public static void w(Object msg) {
        log(mIsDebug, mIsTrack, "", msg, LEVEL_W);
    }

    public static void w(String tag, Object msg) {
        log(mIsDebug, mIsTrack, tag, msg, LEVEL_W);
    }

    public static void e(Object msg) {
        log(mIsDebug, mIsTrack, "", msg, LEVEL_E);
    }

    public static void e(String tag, Object msg) {
        log(mIsDebug, mIsTrack, tag, msg, LEVEL_E);
    }

    public static void e(boolean isTrack, String tag, Object msg) {
        log(mIsDebug, isTrack, tag, msg, LEVEL_E);
    }

    // always print
    public static void print(String tag, Object msg) {
        log(true, mIsTrack, tag, msg, LEVEL_I);
    }

    static String log(boolean isDebug, boolean isTrack, String tag, Object msg, int level) {
        if (isDebug) {
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace.length > 4) {
                StackTraceElement element = stackTrace[4];
                sb.append(getTrace(element));
                sb.append(": ");
                sb.append(msg);
            }

            if (isTrack) {
                for (int i = 5; i < stackTrace.length; i++) {
                    String trace = stackTrace[i].toString();
                    if (trace.contains(mPkgName)) {
                        sb.append("\n");
                        sb.append(i).append("\t");
                        sb.append(getTrace(stackTrace[i]));
                    }
                }
            }

            // print log
            switch (level) {
                case LEVEL_V:
                    Log.v(mTag + tag, sb.toString());
                    break;
                case LEVEL_D:
                    Log.d(mTag + tag, sb.toString());
                    break;
                case LEVEL_I:
                    Log.i(mTag + tag, sb.toString());
                    break;
                case LEVEL_W:
                    Log.w(mTag + tag, sb.toString());
                    break;
                case LEVEL_E:
                    Log.e(mTag + tag, sb.toString());
                    break;
            }
            return mTag + tag + "##" + sb.toString();
        }
        return null;
    }

    private static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    private static String getTrace(StackTraceElement element) {
        StringBuilder sb = new StringBuilder();
        sb.append(getSimpleClassName(element.getClassName()))
                .append(".")
                .append(element.getMethodName())
                .append("(")
                .append(element.getFileName())
                .append(":")
                .append(element.getLineNumber())
                .append(")");
        return sb.toString();
    }
}
