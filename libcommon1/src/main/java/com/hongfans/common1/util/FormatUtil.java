package com.hongfans.common1.util;

import android.content.Context;
import android.text.format.Formatter;

import java.util.Locale;

/**
 * @desc: TODO
 */
public class FormatUtil{

    public static String formatFileSize(Context ctx, long size){
        return Formatter.formatFileSize(ctx, size);
    }


    /**
     * 格式化时长 (格式化为 分:秒)
     */
    public static String formateDuration(int duration){
        return String.format(Locale.CHINESE, "%02d:%02d", duration / 60, duration % 60);
    }
}
