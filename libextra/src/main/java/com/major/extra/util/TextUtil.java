package com.major.extra.util;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

public class TextUtil{

    public static Spannable changeColor(String beforeText, String centerText, String endText, String color){
        return (Spannable)Html.fromHtml(beforeText + "<font color=\"#" + color + "\">" + centerText + "</font>" + endText);
    }

    public static Spannable changeColor(String centerText, String endText, String color){
        return (Spannable)Html.fromHtml("<font color=\"#" + color + "\">" + centerText + "</font>" + endText);
    }

    public static Spannable changeColor(String centerText, String color){
        return (Spannable)Html.fromHtml("<font color=\"#" + color + "\">" + centerText + "</font>");
    }

    /**
     * 返回一个高亮spannable
     *
     * @param content 文本内容
     * @param color   高亮颜色
     * @param start   起始位置
     * @param end     结束位置
     * @return 高亮spannable
     */
    public static CharSequence getHighLightText(String content, int color, int start, int end){
        if(TextUtils.isEmpty(content)){
            return "";
        }
        start = start >= 0 ? start : 0;
        end = end <= content.length() ? end : content.length();
        SpannableString spannable = new SpannableString(content);
        CharacterStyle span = new ForegroundColorSpan(color);
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 获取链接样式的字符串，即字符串下面有下划线
     *
     * @param resId 文字资源
     * @return 返回链接样式的字符串
     */
    public static Spanned getHtmlStyleString(int resId){
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"\"><u><b>").append(UIUtils.getString(resId))
          .append(" </b></u></a>");
        return Html.fromHtml(sb.toString());
    }
}
