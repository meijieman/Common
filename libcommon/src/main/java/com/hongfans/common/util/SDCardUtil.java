package com.hongfans.common.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * TODO
 * Created by MEI on 2017/9/8.
 */

public class SDCardUtil {

    private static final String TAG = "SDCardUtil";

    /**
     * Get SD card path by CMD.
     */
    public static String getSDCardPath(){
        String cmd = "cat /proc/mounts";
        String sdcard = null;
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        BufferedReader bufferedReader = null;
        try{
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getInputStream())));
            String lineStr;
            while((lineStr = bufferedReader.readLine()) != null){
                Log.i(TAG, "proc/mounts:   " + lineStr);
                if(lineStr.contains("sdcard") && lineStr.contains(".android_secure")){
                    String[] strArray = lineStr.split(" ");
                    if(strArray.length >= 5){
                        sdcard = strArray[1].replace("/.android_secure", "");
                        Log.i(TAG, "find sd card path:   " + sdcard);
                        return sdcard;
                    }
                }
                if(p.waitFor() != 0 && p.exitValue() == 1){
                    // p.exitValue()==0表示正常结束，1：非正常结束
                    Log.e(TAG, cmd + " 命令执行失败");
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(bufferedReader != null){
                    bufferedReader.close();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        sdcard = Environment.getExternalStorageDirectory().getPath();
        Log.i(TAG, "not find sd card path return default:   " + sdcard);
        return sdcard;
    }

    /**
     * Get available size
     */
    public static long getAvailableSize(String path){
        try{
            File base = new File(path);
            StatFs stat = new StatFs(base.getPath());
            return stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        } catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获得SD卡总大小
     */
    public static String getSDTotalSize(Context context){
        if(isSdCardMounted()){
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return Formatter.formatFileSize(context, blockSize * totalBlocks);
        }

        return "0";
    }

    public static boolean isSdCardMounted(){
        String storageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(storageState);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static String getSDAvailableSizeFormat(Context context){
        return Formatter.formatFileSize(context, getSDAvailableSize());
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     */
    public static long getSDAvailableSize(){
        if(isSdCardMounted()){
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();

            return blockSize * availableBlocks;
        }

        return 0;
    }
}
