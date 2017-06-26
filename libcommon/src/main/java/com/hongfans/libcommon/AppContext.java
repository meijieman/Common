package com.hongfans.libcommon;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 *
 */
public class AppContext extends Application{

    private static AppContext sInstance;
    private static long sMainThreadId;
    private static Handler sHandler = new Handler();

    public static Context getInstance(){
        return sInstance;
    }

    public static long getMainThreadId(){
        return sMainThreadId;
    }

    public static Handler getHandler(){
        return sHandler;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        sInstance = this;
        sMainThreadId = Thread.currentThread().getId();
    }
}
