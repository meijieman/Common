package com.hongfans.common.util;

import android.app.ActivityManager;
import android.content.Context;

/**
 * TODO
 * Created by MEI on 2017/9/22.
 */

public class AppUtil {

    public static boolean isServiceRunning(Context ctx, String serviceName) {
        if (ctx == null || serviceName == null) {
            return false;
        }
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
