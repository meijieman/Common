package com.hongfans.common.api;

import com.hongfans.common.log.LogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @desc: 无论无网络都添加缓存
 * @author: Major
 * @since: 2017/2/22 0:55
 */
public class NetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        LogUtil.i("intercept");
        Request request = chain.request();
        LogUtil.e("request " + request);
        Response response = chain.proceed(request);
        /*String cacheControl = request.header("Cache-Control");
        if (TextUtils.isEmpty(cacheControl)) {
            cacheControl = "public, max-age=60";
        }*/
        LogUtil.e("request " + request + ", response " + response);
        // 20s内重复请求返回第一次请求的缓存。20s后如果没有网络，则获取不到数据，显示连接失败，有数据则请求服务器
        int maxAge = 20;
        return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-control", "public, max-age=" + maxAge)
                .build();
    }
}
