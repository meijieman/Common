package com.major.base.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 作者:meijie
 * 包名:com.major.base.util
 * 工程名:Common
 * 时间:2018/7/9 9:56
 * 说明: 隐藏，显示键盘
 */
public class KeyboardUtil{

    /**
     * 隐藏软键盘
     * @param context
     * @param view
     */
    public static void hideKeyboard(Context context, View view){
        if(view == null){
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 显示软键盘
     * @param context
     * @param view 接受软键盘输入的视图
     */
    public static void showKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
    }
}
