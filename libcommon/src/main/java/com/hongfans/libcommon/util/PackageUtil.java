package com.hongfans.libcommon.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.hongfans.libcommon.log.LogUtil;

import java.io.File;
import java.util.List;

/**
 * @Desc: TODO
 * @Author: Major
 * @Since: 2016/4/19 12:29
 */
public class PackageUtil{

    private static final String TAG = "PackageUtil";

    // 获取系统版本号(显示给用户)
    public static String getVersionName(Context context){
        try{
            PackageInfo info = getPackageInfo(context);
            if(info != null){
                return info.versionName;
            } else {
                return null;
            }
        } catch(PackageManager.NameNotFoundException e1){
            e1.printStackTrace();
            return null;
        }
    }

    public static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException{
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    }

    // 获取系统版本号(版本更新用)
    public static int getVersionCode(Context context){
        try{
            PackageInfo info = getPackageInfo(context);
            if(info != null){
                return info.versionCode;
            } else {
                return 0;
            }
        } catch(PackageManager.NameNotFoundException e1){
            e1.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据 apk 获取相关信息
     *
     * @param context
     * @param archiveFilePath 安装包路径 archiveFilePath = "sdcard/download/Law.apk"
     */
    public static void getApkInfo(Context context, String archiveFilePath){
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);

        if(info != null){
            ApplicationInfo appInfo = info.applicationInfo;
            String appName = pm.getApplicationLabel(appInfo).toString();
            String packageName = appInfo.packageName; //得到安装包名称
            String version = info.versionName; //得到版本信息
            Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息

            Toast.makeText(context, "packageName:" + packageName + ";version:" + version, Toast.LENGTH_LONG).show();
        }
    }

    // 安装路径
    public static String getInstallPath(Context context){
        return context.getApplicationContext().getFilesDir().getAbsolutePath();
    }

    // 已安装程序列表
    public static List<PackageInfo> getInstalledPathages(Context context){
        return context.getPackageManager().getInstalledPackages(0);
    }

    /**
     * 安装apk应用
     */
    public static void installAPK(Context context, File apkFile){
        if(apkFile.isFile()){
            String fileName = apkFile.getName();
            String postfix = fileName.substring(fileName.length() - 4, fileName.length());
            if(postfix.toLowerCase().equals(".apk")){
                String cmd = "chmod 755 " + apkFile.getAbsolutePath();
                try{
                    Runtime.getRuntime().exec(cmd);
                } catch(Exception e){
                    Log.e(TAG, e.getLocalizedMessage());
                }
                Uri uri = Uri.fromFile(apkFile);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                context.startActivity(intent);
            }
        } else if(apkFile.isDirectory()){
            File[] files = apkFile.listFiles();
            int fileCount = files.length;
            for(File file : files){
                installAPK(context, file);
            }
        }
    }

    /**
     * 安装apk应用
     */
    public static void installDirApk(Context context, String filePath){
        File file = new File(filePath);
        installAPK(context, file);
    }

    /**
     * 卸载apk文件
     */
    public static void uninstallPackage(Context context, Uri packageUri){
        Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
        context.startActivity(intent);
    }

    /**
     * 通过报名获取包信息
     */
    public static PackageInfo getPackageInfoByName(Context context, String packageName){
        if(null == packageName || "".equals(packageName)){
            return null;
        }
        try{
            return context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
        } catch(PackageManager.NameNotFoundException e){
            Log.e(TAG, e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * 判断apk包是否安装
     */
    public static boolean isApkIntalled(Context context, String packageName){
        return null != getApplicationInfoByName(context, packageName);
    }

    /**
     * 根据包得到应用信息
     */
    public static ApplicationInfo getApplicationInfoByName(Context context, String packageName){
        if(null == packageName || "".equals(packageName)){
            return null;
        }
        try{
            return context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
        } catch(PackageManager.NameNotFoundException e){
            LogUtil.e(packageName + " NameNotFoundException");
            return null;
        }
    }
}
