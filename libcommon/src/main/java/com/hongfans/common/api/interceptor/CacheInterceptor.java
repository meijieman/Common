package com.hongfans.common.api.interceptor;

import com.hongfans.common.log.LogUtil;
import com.hongfans.common.util.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @desc: 设置缓存策略
 * @author: Major
 * @since: 2017/2/22 22:30
 */
public class CacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        LogUtil.i("intercept");
        Request request = chain.request();
        LogUtil.e("request " + request);
        // 无网络时强制使用缓存
        if (!NetworkUtils.isConnected()) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            LogUtil.w("无网络，使用缓存");
        }

        Response response = chain.proceed(request);
        LogUtil.e("request " + request + ", response " + response);
        if (NetworkUtils.isConnected()) {
            int maxStale = 30;// 设置缓存
            response = response.newBuilder()
                    // 有网络时设置缓存时间为0，也可以设置一个数值，则在指定时间内使用的是缓存
                    .header("Cache-Control", "public, max-age=" + maxStale)
                    // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .removeHeader("Pragma")
                    .build();
            LogUtil.w("有网络，设置缓存时间为" + maxStale);
        } else {
            int maxStale = 60;// 设置缓存为60s
            response = response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
            LogUtil.w("无网络，设置缓存时间为" + maxStale);
        }

        return response;
    }
}
