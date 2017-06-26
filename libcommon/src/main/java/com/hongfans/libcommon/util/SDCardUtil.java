package com.hongfans.libcommon.util;

import android.os.Environment;
import android.os.StatFs;

public class SDCardUtil{

    public static boolean hasSDcard(){
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 计算sdcard上的剩余空间
     *
     * @return
     */
    public int free(){
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        // (availableBlocks * blockSize)/1024 KIB 单位
        // (availableBlocks * blockSize)/1024 /1024 MIB单位
        double sdFreeMB = ((double)stat.getAvailableBlocks() * (double)stat.getBlockSize()) / (1024 * 1024);
        return (int)sdFreeMB;
    }
}
