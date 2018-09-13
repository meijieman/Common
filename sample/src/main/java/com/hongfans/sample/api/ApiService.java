package com.hongfans.sample.api;

import com.hongfans.sample.Articles;
import com.hongfans.sample.Categories;
import com.major.http.api.rx.RxResp;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

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


    @GET("http://www.wanandroid.com/article/list/{index}/json")
    Observable<RxResp<Articles>> getArticles(@Path("index") int index);

}
