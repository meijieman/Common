package com.major.extra.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by felix on 16/4/27.
 */
public class NetworkUtil{

    /**
     * 判断网络连接是否可用
     */
    public static boolean isConnected(Context context){
        if(context != null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getApplicationContext()
                                                                                   .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if(mNetworkInfo != null){
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     */
    public static boolean isWifiConnected(Context context){
        if(context != null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getApplicationContext()
                                                                                   .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(mWiFiNetworkInfo != null){
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     */
    public static boolean isMobileConnected(Context context){
        if(context != null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getApplicationContext()
                                                                                   .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if(mMobileNetworkInfo != null){
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     *
     * @return -1 出错
     */
    public static int getConnectedType(Context context){
        if(context != null){
            ConnectivityManager manager = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if(info != null && info.isAvailable()){
                return info.getType();
            }
        }
        return -1;
    }
}
