package com.major.base.crash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import com.major.base.util.CloseUtil;
import com.major.base.util.CommonUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressLint({"SimpleDateFormat", "SdCardPath"})
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    public static final String EXTRA_STACK_TRACE = "cat.ereza.customactivityoncrash.EXTRA_STACK_TRACE";
    public static String sDir = "/mnt/sdcard/rearview/"; // 默认路径
    private static CrashHandler instance;
    private UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private Map<String, String> infos = new HashMap<>();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private boolean mIsDebug = true;

    private CrashHandler() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    /**
     * 初始化日志
     *
     * @param context
     * @param dir
     * @param isDebug
     */
    public void init(Context context, String dir, boolean isDebug) {
        mContext = context;
        mIsDebug = isDebug;
        if (CommonUtil.isNotEmpty(dir)) {
            sDir = dir;
        }
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        boolean isHandled = handleException(ex);
        if (!isHandled && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //收集设备参数信息
        collectDeviceInfo(mContext);
        //write failed: ENOSPC (No space left on device)
//        long availableSize = SDCardUtil.getSDAvailableSize();
//        if (availableSize < 1024 * 1024) {
//            return false;
//        }
        //保存日志文件
        saveCatchInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     */
    private String saveCatchInfo2File(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        if (ex == null) {
            ex = new Throwable("exception 为 null");
        }
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        try {
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + ".log";
            File dir = new File(sDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(sDir + File.separator + fileName);
            fos.write(sb.toString().getBytes());

            sendCrashLog2PM(sDir + File.separator + fileName);
            fos.close();

            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        } finally {
            if (mIsDebug) {
                Intent intent = new Intent(mContext, DefaultErrorActivity.class);
                intent.putExtra(EXTRA_STACK_TRACE, sb.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
            }
        }

        return null;
    }

    private void sendCrashLog2PM(String fileName) {
        if (!new File(fileName).exists()) {
            //	Toast.makeText(mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
            return;
        }
        FileInputStream fis = null;
        BufferedReader reader = null;
        String s = null;
        try {
            fis = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            while (true) {
                s = reader.readLine();
                if (s == null) {
                    break;
                }
                Log.e("info", s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(reader, fis);
        }
    }
}