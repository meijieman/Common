package com.hongfans.common1.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil{

    private static final ThreadPoolUtil manager = new ThreadPoolUtil();
    private ExecutorService service;

    private ThreadPoolUtil(){
        int num = Runtime.getRuntime().availableProcessors();
        service = Executors.newFixedThreadPool(num * 2);
    }

    public static ThreadPoolUtil getInstance(){
        return manager;
    }

    public void addTask(Runnable runnable){
        service.execute(runnable);
    }
}
