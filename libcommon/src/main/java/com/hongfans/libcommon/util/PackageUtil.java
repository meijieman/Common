package com.hongfans.libcommon.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.hongfans.libcommon.log.LogUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Desc: TODO
 * @Author: Major
 * @Since: 2016/4/19 12:29
 */
public class PackageUtil{

    /**
     * App installation location flags of android system
     */
    public static final int APP_INSTALL_AUTO = 0;
    public static final int APP_INSTALL_INTERNAL = 1;
    public static final int APP_INSTALL_EXTERNAL = 2;
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

    /**
     * 调用系统安装应用
     */
    public static boolean install(Context context, File file){
        if(file == null || !file.exists() || !file.isFile()){
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    /**
     * 调用系统卸载应用
     */
    public static void uninstallApk(Context context, String packageName){
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    /**
     * 打开已安装应用的详情
     */
    public static void goToInstalledAppDetails(Context context, String packageName){
        Intent intent = new Intent();
        int sdkVersion = Build.VERSION.SDK_INT;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", packageName, null));
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra((sdkVersion == Build.VERSION_CODES.FROYO ? "pkg"
                                                                     : "com.android.settings.ApplicationPkgName"), packageName);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 获取指定程序信息
     */
    public static ActivityManager.RunningTaskInfo getTopRunningTask(Context context){
        try{
            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            // 得到当前正在运行的任务栈
            List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
            // 得到前台显示的任务栈
            ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
            return runningTaskInfo;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static String getAppVersionName(Context context){
        try{
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return "";
    }

    public static int getAppVersionCode(Context context){
        try{
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * get app package info
     */
    public static PackageInfo getAppPackageInfo(Context context){
        if(context != null){
            PackageManager pm = context.getPackageManager();
            if(pm != null){
                PackageInfo pi;
                try{
                    return pm.getPackageInfo(context.getPackageName(), 0);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * whether context is system application
     */
    public static boolean isSystemApplication(Context context){
        if(context == null){
            return false;
        }
        return isSystemApplication(context, context.getPackageName());
    }

    /**
     * whether packageName is system application
     */
    public static boolean isSystemApplication(Context context, String packageName){
        PackageManager packageManager = context.getPackageManager();
        if(packageManager == null || packageName == null || packageName.length() == 0){
            return false;
        }
        try{
            ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取已安装的全部应用信息
     */
    public static boolean isInsatalled(Context context, String pkg){
        if(!StringUtil.isEmpty(pkg)){
            List<PackageInfo> list = getInsatalledPackages(context);
            if(list != null){
                for(PackageInfo pi : list){
                    if(pkg.equalsIgnoreCase(pi.packageName)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取已安装的全部应用信息
     */
    public static List<PackageInfo> getInsatalledPackages(Context context){
        return context.getPackageManager().getInstalledPackages(0);
    }

    /**
     * 获取指定程序信息
     */
    public static ApplicationInfo getApplicationInfo(Context context, String pkg){
        try{
            return context.getPackageManager().getApplicationInfo(pkg, 0);
        } catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定程序信息
     */
    public static PackageInfo getPackageInfo(Context context, String pkg){
        try{
            return context.getPackageManager().getPackageInfo(pkg, 0);
        } catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 启动应用
     */
    public static boolean startAppByPackageName(Context context, String packageName){
        return startAppByPackageName(context, packageName, null);
    }

    /**
     * 启动应用
     */
    public static boolean startAppByPackageName(Context context, String packageName, Map<String, String> param){
        PackageInfo pi = null;
        try{
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT){
                resolveIntent.setPackage(pi.packageName);
            }

            List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

            ResolveInfo ri = apps.iterator().next();
            if(ri != null){
                String packageName1 = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                ComponentName cn = new ComponentName(packageName1, className);

                intent.setComponent(cn);
                if(param != null){
                    for(Map.Entry<String, String> en : param.entrySet()){
                        intent.putExtra(en.getKey(), en.getValue());
                    }
                }
                context.startActivity(intent);
                return true;
            }
        } catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context.getApplicationContext(), "启动失败",
                    Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
