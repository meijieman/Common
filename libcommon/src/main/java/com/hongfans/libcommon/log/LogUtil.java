package com.hongfans.libcommon.log;

import android.os.Environment;
import android.support.annotation.IntDef;
import android.util.Log;

import com.hongfans.libcommon.util.FileUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @desc: TODO
 * @author: Major
 * @since: 2017/6/26 23:06
 */
public class LogUtil{

    private static final int LEVEL_V = 1;
    private static final int LEVEL_D = 2;
    private static final int LEVEL_I = 3;
    private static final int LEVEL_W = 4;
    private static final int LEVEL_E = 5;

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

    public static void v(String msg){
        log(mIsDebug, "", msg, LEVEL_V);
    }

    public static void v(String tag, String msg){
        log(mIsDebug, tag, msg, LEVEL_V);
    }


    public static void d(String msg){
        log(mIsDebug, "", msg, LEVEL_D);
    }

    public static void d(String tag, String msg){
        log(mIsDebug, tag, msg, LEVEL_D);
    }

    public static void i(String msg){
        log(mIsDebug, "", msg, LEVEL_I);
    }

    public static void i(String tag, String msg){
        log(mIsDebug, tag, msg, LEVEL_I);
    }

    public static void w(String msg){
        log(mIsDebug, "", msg, LEVEL_W);
    }

    public static void w(String tag, String msg){
        log(mIsDebug, tag, msg, LEVEL_W);
    }

    public static void e(String msg){
        log(mIsDebug, "", msg, LEVEL_E);
    }

    public static void e(String tag, String msg){
        log(mIsDebug, tag, msg, LEVEL_E);
    }

    // always print
    public static void print(String tag, String msg){
        log(true, tag, msg, LEVEL_I);
    }

    public static void saveLog(String msg){
        String log = log(mIsDebug, "", msg, LEVEL_I);
        // save to file
        String parent = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileUtil.saveFile(log, parent, "log", true);
    }

    /**
     * 保存 log 到本地
     */
    public static void saveLog(String filename, String tag, String msg){
        String log = log(mIsDebug, tag, msg, LEVEL_I);
        // save to file
        String parent = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileUtil.saveFile(log, parent, filename, true);
    }

    private static String log(boolean isDebug, String tag, String msg, @Level int level){
        if(isDebug){
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StackTraceElement element = stackTrace[4];
            String pkgName = getPkgName(element.getClassName()); // 获取包名
            sb.append(element.toString().replace(pkgName + ".", ""));
            sb.append(": ");
            sb.append(msg);

            if(mIsTrack){
                for(int i = 5; i < stackTrace.length; i++){
                    String trace = stackTrace[i].toString();
                    if(trace.contains(pkgName)){
                        sb.append("\n");
                        sb.append(i).append("\t");
                        sb.append(trace);
                    }
                }
            }

            // print log
            switch(level) {
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

    private static String getPkgName(String className){
        if(className.contains("$")){
            // inner class
            className = className.substring(0, className.indexOf("$"));
        }
        className = className.substring(0, className.lastIndexOf("."));

        return className;
    }

    @Retention(RetentionPolicy.CLASS)
    @IntDef({LEVEL_V, LEVEL_D, LEVEL_I, LEVEL_W, LEVEL_E})
    private @interface Level{}
}
