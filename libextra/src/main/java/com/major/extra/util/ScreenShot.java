package com.major.extra.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * view 截图实现
 */
public class ScreenShot{

    private static final String TAG = ScreenShot.class.getSimpleName();

    public static String shot(Activity a){
        savePic(takeScreenShot(a), "sdcard/maizuo_xx.png");
        return "/maizuo_xx.png";
    }

    //保存到sdcard   
    private static void savePic(Bitmap b, String strFileName){
        FileOutputStream fos;
        try{
            fos = new FileOutputStream(strFileName);
            b.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // 获取指定Activity的截屏，保存到png文件
    private static Bitmap takeScreenShot(Activity activity){
        //View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        //获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        //      int statusBarHeight = frame.height() - view.getHeight();

        //获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Log.d(TAG, "Rect height:" + frame.height() + " Rect width:	" + frame.width());
        Log.d(TAG, "view height:" + view.getHeight() + " view width:	" + view.getWidth());
        Log.d(TAG, "screen_shot:" + (width - 40) + " " + statusBarHeight + " " + 40 + " " + (height - statusBarHeight));

        //去掉标题栏
        //Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        //        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight); //全屏
        Bitmap b = Bitmap.createBitmap(b1, width - 40, statusBarHeight * 2, 40, height - statusBarHeight * 2); //全屏
        view.destroyDrawingCache();
        return b;
    }
}
