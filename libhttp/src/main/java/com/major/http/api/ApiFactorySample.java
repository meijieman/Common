package com.major.http.api;

import android.os.Environment;

import com.major.base.log.LogUtil;
import com.major.http.api.interceptor.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ApiFractory 为演示 okhttp + retrofit 的使用
 * Created by MEI on 2018/1/16.
 */

public class ApiFactorySample {

    public static final String BASE_URL = "https://github.com";

    private final Retrofit mRetrofit;

    private ApiFactorySample() {
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
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static ApiFactorySample getInstance() {
        return HOLDER.sInstance;
    }

    public <T> T getService(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    private static class HOLDER {
        private static final ApiFactorySample sInstance = new ApiFactorySample();
    }
}
