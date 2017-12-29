package com.hongfans.common1.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil{

    /**
     * 获得网络连接是否可用
     *
     * @param context
     * @return
     */
    public static boolean hasNetwork(Context context){
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return !(info == null || !info.isAvailable());
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean checkWifiState(Context context){
        boolean isWifiConnect = true;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] infos = cm.getAllNetworkInfo();
        for(NetworkInfo info : infos){
            if(info.getState() == NetworkInfo.State.CONNECTED){
                if(info.getType() == ConnectivityManager.TYPE_MOBILE){
                    isWifiConnect = false;
                }
                if(info.getType() == ConnectivityManager.TYPE_WIFI){
                    isWifiConnect = true;
                }
            }
        }
        return isWifiConnect;
    }

    /**
     * 打开网络设置界面
     */
    public static void openNet(Activity activity){
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }
}
