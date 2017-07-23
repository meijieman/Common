package com.hongfans.libcommon.util;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;

import com.hongfans.libcommon.log.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 常用工具集合类
 */
public class CommonUtil{

    private static List<Long> countList = new ArrayList<>();

    /**
     * @return if list is null or its size is 0, return true, else return false.
     */
    public static <V> boolean isEmpty(List<V> sourceList){
        return (sourceList == null || sourceList.size() == 0);
    }

    public static int toInt(String strValue){
        int iValue = 0;
        try{
            iValue = Integer.parseInt(strValue);
        } catch(Exception e){
            e.printStackTrace();
            LogUtil.e("转换 int 失败 原始 " + strValue);
        }
        return iValue;
    }

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

    public static boolean writeFileToSD(String path, String logstr){
        FileUtil.createFolderIfNotExist(path);
        File file = new File(path);
        File[] filelist = file.listFiles();
        int size = filelist.length;
        try{
            if(size <= 0){
                String fileName = CreateSysTimeFileName();
                return FileUtil.saveFile(logstr, path, fileName, false);
            } else {
                for(int i = 0; i < size; i++){
                    if(filelist[i].length() < 512000){
                        return FileUtil.saveFile(logstr, path, filelist[i].getName(), true);
                    }
                }
                String fileName = CreateSysTimeFileName();
                return FileUtil.saveFile(logstr, path, fileName, true);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static String CreateSysTimeFileName(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate) + ".txt";
    }

    /**
     * 给view设置margin
     */
    public static void setMargins(View v, int l, int t, int r, int b){
        if(v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /**
     * 将项目下的数据库拷贝到sd卡中
     */
    public static boolean copyDbToSdCard(Context ctx){
        FileInputStream fis = null;
        FileOutputStream ops = null;
        try{
            File file = ctx.getFilesDir();
            File dir = new File(file.getParent(), "databases");
            File db = new File(dir, "hongfan.db");
            File sddb = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "hfcyb.db3");
            fis = new FileInputStream(db);
            ops = new FileOutputStream(sddb);
            byte[] buffer = new byte[4096];
            int index = 0;
            while((index = fis.read(buffer)) != -1){
                ops.write(buffer, 0, index);
            }
            ops.flush();
            ops.close();
            fis.close();
        } catch(FileNotFoundException e){
            return false;
        } catch(IOException e){
            return false;
        } finally {
            try{
                if(ops != null){
                    ops.close();
                    ops = null;
                }
            } catch(IOException e){
                e.printStackTrace();
            }
            try{
                if(fis != null){
                    fis.close();
                    fis = null;
                }
            } catch(IOException e){

            }
        }
        return true;
    }
}
