package com.major.extra.util;

import android.os.Handler;

/**
 * 安全更新 ui 的操作的类
 */
public class TaskUtil{

    private static Handler sHandler;
    private static long sMainThreadId;

    /**
     * 在 Application#onCreate 中初始化，得到一个主线程的 handler
     */
    public static void init(){
        sHandler = new Handler();
        sMainThreadId = Thread.currentThread().getId();
    }

    /**
     * 安全的执行一个task
     */
    public static void postTaskSafely(Runnable task){
        long curThreadId = android.os.Process.myTid();
        // 如果当前线程是主线程
        if(curThreadId == sMainThreadId){
            task.run();
        } else {
            if(sHandler == null){
                throw new RuntimeException("未调用 init()");
            }
            // 如果当前线程不是主线程
            sHandler.post(task);
        }
    }
}
