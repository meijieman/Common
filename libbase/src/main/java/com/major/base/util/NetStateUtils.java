package com.major.base.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.major.base.log.LogUtil;

/**
 * Created by 刘亚星 on 2017/8/2.
 */
@TargetApi(24)
public class NetStateUtils {
    private static final int NETWORK_TYPE_UNAVAILABLE = -1;
    private static final int NETWORK_TYPE_WIFI = -101;
    private static final int NETWORK_CLASS_WIFI = -101;
    private static final int NETWORK_CLASS_UNAVAILABLE = -1;
    /**
     * 不清楚数据流量类型
     */
    private static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * 数据流量类型为2G
     */
    private static final int NETWORK_CLASS_2_G = 1;
    /**
     * 数据流量类型为3G
     */
    private static final int NETWORK_CLASS_3_G = 2;
    /**
     * 数据流量类型为4G .
     */
    private static final int NETWORK_CLASS_4_G = 3;
    // 适配低版本手机
    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;
    //是否打开了移动数据流量
    private static boolean mobileNetwork = false;
    //是否打开了无线
    private static boolean wifiNetwork = false;
    private static Context context;

    private static NetStateUtils utils;

    private NetStateUtils(Context context) {
        this.context = context;
    }

    public static NetStateUtils getUtils(Context context) {
        if (utils == null) {
            utils = new NetStateUtils(context);
            return utils;
        }
        return utils;
    }

    public static String getInfos() {
        // 运营商
        String provider;
        // 数据流量类型 --- 2G,3G,4G
        String type;
        // 手机品牌型号
        String model;
        provider = getProvider(context);
        model = android.os.Build.MODEL;
        type = getNetworkInfo();
        return provider + type + model;
    }

    // 运营商
    public String getProvider() {
        return getProvider(context);
    }

    // 数据流量类型 --- 2G,3G,4G,wifi
    public String getNetType() {
        return getNetworkInfo();
    }

    // 手机品牌型号
    public String getModel() {
        return android.os.Build.MODEL;
    }

    public String getVersionName() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取运营商
     * 中国移动 46000 46002 46007 46020 中国联通 46001 46006 46009 中国电信 46003 46005 46011
     *
     * @return
     */
    public static String getProvider(Context context) {
        String provider = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                String IMSI = telephonyManager.getSubscriberId();
                LogUtil.i("imsi " + IMSI);
                if (StringUtil.isEmpty(IMSI) || IMSI.length() < 5) {
                    if (TelephonyManager.SIM_STATE_READY == telephonyManager
                            .getSimState()) {
                        String operator = telephonyManager.getSimOperator();
                        if (StringUtil.isNotEmpty(operator) && operator.length() >= 5) {
                            return operator.substring(0, 5);
                        }
                    }
                } else {
                    return IMSI.substring(0, 5);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provider;
    }

    private static String getNetworkInfo() {
        String type = "";
        // 获取网络连接情况
        ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = mConnectivity
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetworkInfo = mConnectivity
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mobileNetworkInfo != null && mobileNetworkInfo.isConnected()) {
            mobileNetwork = true;
            type = getCurrentNetworkType(context);
        } else {
            mobileNetwork = false;
        }
        if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
            wifiNetwork = true;
            type = getCurrentNetworkType(context);
        } else {
            wifiNetwork = false;
        }
        return type;
    }

    private static int getNetworkClassByType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return NETWORK_CLASS_UNAVAILABLE;
            case NETWORK_TYPE_WIFI:
                return NETWORK_CLASS_WIFI;
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public static String getCurrentNetworkType(Context context) {
        int networkClass = getNetworkClass(context);
        String type = "";
        switch (networkClass) {
            case NETWORK_CLASS_UNAVAILABLE:
                type = "";
                break;
            case NETWORK_CLASS_WIFI:
                type = "Wi-Fi";
                break;
            case NETWORK_CLASS_2_G:
                type = "2G";
                break;
            case NETWORK_CLASS_3_G:
                type = "3G";
                break;
            case NETWORK_CLASS_4_G:
                type = "4G";
                break;
            case NETWORK_CLASS_UNKNOWN:
                type = "";
                break;
        }
        return type;
    }

    private static int getNetworkClass(Context context) {
        int networkType = NETWORK_TYPE_UNKNOWN;
        try {
            final NetworkInfo network = ((ConnectivityManager)
                    context
                            .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
                            Context.TELEPHONY_SERVICE);
                    networkType = telephonyManager.getNetworkType();
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getNetworkClassByType(networkType);

    }
}
