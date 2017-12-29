package com.hongfans.common1.util;

import android.app.Activity;

import java.util.Stack;

public class ActivityStackUtil{

    private static Stack<Activity> activityStack;

    private static ActivityStackUtil sInstance;

    private ActivityStackUtil(){

    }

    public static ActivityStackUtil getManager(){
        if(sInstance == null){
            sInstance = new ActivityStackUtil();
        }
        return sInstance;
    }

    public boolean popActivity(){
        Activity activity = activityStack.lastElement();
        if(activity != null){
            activity.finish();
            return true;
        }
        return false;
    }

    public void pushActivity(Activity activity){
        if(activityStack == null){
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public void popAllActivityExceptOne(Class cls){
        while(true){
            Activity activity = currentActivity();
            if(activity == null){
                break;
            }
            if(activity.getClass().equals(cls)){
                break;
            }
            popActivity(activity);
        }
    }

    public Activity currentActivity(){
        if(!activityStack.empty()){
            return activityStack.lastElement();
        }
        return null;
    }

    public boolean popActivity(Activity activity){
        if(activity != null){
            activity.finish();
            return activityStack.remove(activity);
        }
        return false;
    }
}