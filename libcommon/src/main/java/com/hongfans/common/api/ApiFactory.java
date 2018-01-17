package com.hongfans.common.api;

import android.content.Context;

import com.hongfans.common.api.interceptor.HttpLoggingInterceptor;
import com.hongfans.common.log.LogUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * TODO
 * Created by MEI on 2018/1/16.
 */

public class ApiFactory {

    // FIXME: 2018/1/17 待完善

    public static void test(Context ctx) {
        new ApiFactory.Builder(ctx)
                .url("")
                .okHttpBuilder()
                .retrofitBuilder()
                .build();
    }

    private static Retrofit mRetrofit;

    private ApiFactory() {

    }

    public static class Builder {

        private OkHttpClient.Builder okHttpBuilder;
        private Retrofit.Builder retrofitBuilder;
        private String baseUrl;
        private Context context;

        public Builder(Context ctx) {
            context = ctx.getApplicationContext();
        }

        public Builder url(String url) {
            baseUrl = url;
            return this;
        }

        public Builder okHttpBuilder() {
            okHttpBuilder = okHttp(context);
            return this;
        }

        public Builder retrofitBuilder() {
            if (okHttpBuilder == null) {
                throw new RuntimeException("invoke ApiFactory.Builder#okHttpBuilder() first");
            }
            if (baseUrl == null) {
                throw new RuntimeException("invoke ApiFactory.Builder#url() first");
            }
            retrofitBuilder = retrofit(baseUrl, okHttpBuilder.build());
            return this;
        }

        public void build() {
            mRetrofit = retrofitBuilder.build();
        }

        private OkHttpClient.Builder okHttp(Context context) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            int size = 1024 * 1024 * 10;
            File cacheFile = new File(context.getCacheDir(), "okHttp");
            LogUtil.w("cacheFile " + cacheFile.getAbsolutePath());
            Cache cache = new Cache(cacheFile, size);

            return new OkHttpClient.Builder()
                    .connectTimeout(12, TimeUnit.SECONDS)
                    .readTimeout(12, TimeUnit.SECONDS)
                    .writeTimeout(12, TimeUnit.SECONDS)
                    //                .retryOnConnectionFailure(true)
                    //                .addNetworkInterceptor(new NetworkInterceptor())
                    //                .addInterceptor(new CacheInterceptor())
                    //                .addNetworkInterceptor(new CacheInterceptor())
                    .addInterceptor(loggingInterceptor)
                    .cache(cache);
        }

        private Retrofit.Builder retrofit(String baseURL, OkHttpClient client) {
            return new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        }

    }


    public <T> T getService(Class<T> clazz) {
        if (mRetrofit == null) {
            throw new RuntimeException("ApiFactory.Builder#build() first");
        }
        return mRetrofit.create(clazz);
    }
}
