package com.hongfans.common.qt;

import com.hongfans.common.api.HttpLoggingInterceptor;
import com.hongfans.common.base.CommonConfig;
import com.hongfans.common.log.LogUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * TODO
 * Created by MEI on 2017/10/12.
 */

public class QTFactory {
    private volatile static QTFactory sInstance;

    private Retrofit mRetrofit;
    private Retrofit mRetrofitString;
    private final OkHttpClient client;

    private QTFactory() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        int size = 1024 * 1024 * 10;
        File cacheFile = new File(CommonConfig.getContext().getCacheDir(), "okHttp");
        LogUtil.w("cacheFile " + cacheFile.getAbsolutePath());
        Cache cache = new Cache(cacheFile, size);

        client = new OkHttpClient.Builder()
                .connectTimeout(12, TimeUnit.SECONDS)
                .writeTimeout(12, TimeUnit.SECONDS)
                //                .retryOnConnectionFailure(true)
                //                .addNetworkInterceptor(new NetworkInterceptor())
                //                .addInterceptor(new CacheInterceptor())
                //                .addNetworkInterceptor(new CacheInterceptor())
//                .addInterceptor(new SignInterceptor())
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();


        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://api.open.qingting.fm/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mRetrofitString = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://api.open.qingting.fm/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static QTFactory getInstance() {
        if (sInstance == null) {
            synchronized (QTFactory.class) {
                sInstance = new QTFactory();
            }
        }
        return sInstance;
    }

    public QTApi getQTApi() {
        return mRetrofit.create(QTApi.class);
    }

    public QTApi getQTApiString() {
        return mRetrofitString.create(QTApi.class);
    }
}
