package com.hongfans.sample.api;

import com.hongfans.sample.Categories;
import com.major.http.api.rx.RxResp;

import retrofit2.http.GET;
import rx.Observable;

/**
 * 作者:meijie
 * 包名:com.hongfans.sample.api
 * 工程名:Common
 * 时间:2018/7/5 17:01
 * 说明:
 */
public interface ApiService{

    @GET("categories")
    Observable<RxResp<Categories>> getCategories();

}
