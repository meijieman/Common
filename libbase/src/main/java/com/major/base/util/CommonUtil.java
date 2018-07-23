package com.major.base.util;

import android.content.Intent;

import com.major.base.log.LogUtil;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @描述 常用工具集合类
 * @时间 2016/8/20
 */
public class CommonUtil{

    public static boolean isEmpty(List list){
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(List list){
        return !isEmpty(list);
    }

    public static boolean isEmpty(String str){
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static int toInt(String strValue){
        return toInt(strValue, 0);
    }

    public static int toInt(String strValue, int def){
        int iValue = def;
        try{
            iValue = Integer.parseInt(strValue);
        }catch(Exception e){
//            e.printStackTrace();
            LogUtil.e("转换 int 失败 原始 \"" + strValue + "\"");
        }
        return iValue;
    }

    public static long toLong(String strValue){
        return toLong(strValue, 0L);
    }

    public static long toLong(String strValue, long def){
        long iValue = def;
        try{
            iValue = Long.parseLong(strValue);
        }catch(Exception e){
//            e.printStackTrace();
            LogUtil.e("转换 long 失败 原始 \"" + strValue + "\"");
        }
        return iValue;
    }

//    public static String parseIntent(Intent intent){
//        StringBuilder sb = new StringBuilder();
//        if(intent != null){
//            sb.append("act=").append(intent.getAction()).append(", ");
//            sb.append("cat=").append(intent.getCategories()).append(", ");
//            Bundle bundle = intent.getExtras();
//            if(bundle != null){
//                sb.append(bundle);
//            }
//        }
//        return sb.toString();
//    }

    public static String toUri(Intent intent){
        return intent != null ? URLDecoder.decode(intent.toUri(0)) : null;
    }

    private static List<Long> countList = new ArrayList<>();

    /**
     * 指定时间内执行的次数
     *
     * @param count 指定次数
     * @param time  指定时间 单位 ms
     * @return true  time 内执行了 count 次
     */
    public static boolean isClickAvailable(int count, long time){
        countList.add(System.currentTimeMillis());
        if(countList.size() > count){
            countList.remove(0);
        }
        if(countList.size() == count){
            if((countList.get(count - 1) - countList.get(0)) <= time){
                countList.clear();
                return true;
            }
        }
        return false;
    }

}
