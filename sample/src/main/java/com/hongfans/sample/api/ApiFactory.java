package com.hongfans.sample.api;

import android.os.Environment;

import com.major.base.log.LogUtil;
import com.major.http.api.interceptor.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 作者:meijie
 * 包名:com.hongfans.sample.api
 * 工程名:Common
 * 时间:2018/7/5 17:07
 * 说明: 参照 {@link: com.major.http.api.ApiFactorySample}
 */
public class ApiFactory{

    public static final String BASE_URL = "http://gank.io/api/xiandu/";

    private final Retrofit mRetrofit;

    private static class HOLDER {
        private static final ApiFactory sInstance = new ApiFactory();
    }

    public static ApiFactory getInstance() {
        return HOLDER.sInstance;
    }

    private ApiFactory() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        int size = 1024 * 1024 * 10; // 10M
        Cache cache = new Cache(Environment.getDownloadCacheDirectory(), size);
        LogUtil.w("cacheFile " + cache.directory().getAbsolutePath());

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(12, TimeUnit.SECONDS)
                .readTimeout(12, TimeUnit.SECONDS)
                .writeTimeout(12, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true)
//                .addNetworkInterceptor(new NetworkInterceptor())
//                .addInterceptor(new CacheInterceptor())
//                .addNetworkInterceptor(new CacheInterceptor())
//                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public <T> T getService(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }


}
