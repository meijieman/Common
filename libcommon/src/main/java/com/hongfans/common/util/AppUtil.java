package com.hongfans.common.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Process;

import java.util.List;

/**
 * TODO
 * Created by MEI on 2017/9/22.
 */

public class AppUtil {

    public static boolean isServiceRunning(Context ctx, String serviceName) {
        if (ctx == null || serviceName == null) {
            return false;
        }
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是主进程
     */
    public static boolean isMainProcess(Context context){
        String procName = getCurrentProcessName(context);
        return procName == null || procName.equalsIgnoreCase(context.getPackageName());
    }

    /**
     * 获取当前进程名字
     */
    public static String getCurrentProcessName(Context context){
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()){
            if(appProcess.pid == pid){
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 调用系统分享
     */
    public static void shareToOtherApp(Context context, String title, String content, String dialogTitle){
        Intent intentItem = new Intent(Intent.ACTION_SEND);
        intentItem.setType("text/plain");
        intentItem.putExtra(Intent.EXTRA_SUBJECT, title);
        intentItem.putExtra(Intent.EXTRA_TEXT, content);
        intentItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intentItem, dialogTitle));
    }

    /**
     * need < uses-permission android:name =“android.permission.GET_TASKS” />
     * 判断是否前台运行
     */
    public static boolean isRunningForeground(Context context){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if(taskList != null && !taskList.isEmpty()){
            ComponentName componentName = taskList.get(0).topActivity;
            if(componentName != null && componentName.getPackageName().equals(context.getPackageName())){
                return true;
            }
        }
        return false;
    }

    public static void killTheApp(){
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    /**
     * 获取App包 信息版本号
     */
    public PackageInfo getPackageInfo(Context context){
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try{
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return packageInfo;
    }
}
