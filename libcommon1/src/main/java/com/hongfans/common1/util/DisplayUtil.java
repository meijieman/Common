package com.hongfans.common1.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * TODO
 */
public class DisplayUtil{

    /**
     * 获取 显示信息
     */
    public static DisplayMetrics getDisplayMetrics(Context context){
        return context.getResources().getDisplayMetrics();
    }
}
