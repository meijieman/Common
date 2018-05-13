package com.major.base.util;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * TODO
 * Created by MEI on 2017/6/8.
 */

public final class ToastUtil{

    private static Toast sToast;
    private static Context sContext;

    public static void init(Context ctx){
        if(ctx == null){
            return;
        }
        sContext = ctx.getApplicationContext();
    }

    public static void showShort(String msg){
        showToast(sContext, msg, Toast.LENGTH_SHORT);
    }

    private static void showToast(Context ctx, String msg, int duration){
        if(sContext == null){
            throw new RuntimeException("invoke init() first, and param ctx cannot be null");
        }
        if(msg == null){
            return;
        }
        if(sToast != null){
            sToast.cancel();
        }
        if(Looper.myLooper() == Looper.getMainLooper()){
            // UI 线程
            sToast = Toast.makeText(ctx, "", duration);
            sToast.setText(msg);
            sToast.show();
        } else {
            // 其他线程
            Looper.prepare();
            sToast = Toast.makeText(ctx, "", duration);
            sToast.setText(msg);
            sToast.show();
            Looper.loop(); // 以上三行需在 looper 里面
        }
    }

    public static void showLong(String msg){
        showToast(sContext, msg, Toast.LENGTH_SHORT);
    }
}
