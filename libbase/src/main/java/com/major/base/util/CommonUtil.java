package com.major.base.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.major.base.log.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @描述 常用工具集合类
 * @作者 tll
 * @时间 2016/8/20
 */
public class CommonUtil {

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str) || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static int toInt(String strValue) {
        int iValue = 0;
        try {
            iValue = Integer.parseInt(strValue);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("转换 int 失败 原始 " + strValue);
        }
        return iValue;
    }

    public static String parseIntent(Intent intent) {
        StringBuilder sb = new StringBuilder();
        if (intent != null) {
            sb.append("act=").append(intent.getAction()).append(", ");
            sb.append("cat=").append(intent.getCategories()).append(", ");
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                sb.append(bundle);
            }
        }
        return sb.toString();
    }

    private static List<Long> countList = new ArrayList<>();

    /**
     * 指定时间内执行的次数
     *
     * @param count 指定次数
     * @param time  指定时间 单位 ms
     * @return true  time 内执行了 count 次
     */
    public static boolean isClickAvailable(int count, long time) {
        countList.add(System.currentTimeMillis());
        if (countList.size() > count) {
            countList.remove(0);
        }
        if (countList.size() == count) {
            if ((countList.get(count - 1) - countList.get(0)) <= time) {
                countList.clear();
                return true;
            }
        }
        return false;
    }

    public static long toLong(String strValue) {
        long iValue = 0;
        try {
            iValue = Long.parseLong(strValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iValue;
    }

    public static int getDimens(Context context, int dimenId) {
        return context.getResources().getDimensionPixelOffset(dimenId);
    }

    public static boolean writeFileToSD(String path, String logstr) {
        FileHelp.createFolderIfNotExist(path);
        File file = new File(path);
        File[] filelist = file.listFiles();
        int size = filelist.length;
        try {
            if (size <= 0) {
                String fileName = CreateSysTimeFileName();
                return FileHelp.saveFile(logstr, path, fileName, false);
            } else {
                for (int i = 0; i < size; i++) {
                    if (filelist[i].length() < 512000) {
                        return FileHelp.saveFile(logstr, path, filelist[i].getName(), true);
                    }
                }
                String fileName = CreateSysTimeFileName();
                return FileHelp.saveFile(logstr, path, fileName, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String CreateSysTimeFileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate) + ".txt";
    }

    /**
     * @param
     * @描述 快速模糊化处理bitmap
     * @作者 tll
     * @时间 2016/12/5 19:22
     */
    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int temp = 256 * divsum;
        int dv[] = new int[temp];
        for (i = 0; i < temp; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    /**
     * 判断当前设备是否为低配置的
     */
    public static boolean isLowConfig() {
        if (getTotalMemory() <= 1024) {
            return true;
        }
        return false;
    }

    public static long getTotalMemory() {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) / 1024L;//返回M
            //            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024l;//返回字节
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 给view设置margin
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /**
     * 将项目下的数据库拷贝到sd卡中
     */
    /*public static boolean copyDbToSdCard() {
        FileInputStream fis = null;
        FileOutputStream ops = null;
        try {
            File file = BaseApplication.getInstance().getFilesDir();
            File dir = new File(file.getParent(), "databases");
            File db = new File(dir, "hongfan.db");
            File sddb = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "hfcyb.db3");
            fis = new FileInputStream(db);
            ops = new FileOutputStream(sddb);
            byte[] buffer = new byte[4096];
            int index = 0;
            while ((index = fis.read(buffer)) != -1) {
                ops.write(buffer, 0, index);
            }
            ops.flush();
            ops.close();
            fis.close();
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (ops != null) {
                    ops.close();
                    ops = null;
                }
            } catch (IOException e) {

            }
            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            } catch (IOException e) {

            }
        }
        return true;
    }*/
    public static long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    public static long getAvailMemory(Context context) {// 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static Bitmap createRepeater(int width, Bitmap src) {
        int count = (width + src.getWidth() - 1) / src.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < count; ++idx) {
            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }
        return bitmap;
    }

    /**
     * 释放view的内存
     */
    public static void unbindDrawables(View view) {
        try {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                ((ViewGroup) view).removeAllViews();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
