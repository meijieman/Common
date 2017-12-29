package com.hongfans.common.qt;

import com.hongfans.common.qt.bean.QTDomainList;
import com.hongfans.common.qt.bean.QTToken;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * TODO
 * Created by MEI on 2017/10/12.
 */

public interface QTApi {

    @POST
    Observable<QTToken> getQTToken(@Url String url);

    @GET("http://api.open.qingting.fm/v6/media/mediacenterlist")
    Observable<QTDomainList> getQTDomainList(@Query("access_token") String access_token);

    @GET
    Observable<String> pingQTDomain(@Url String url);

}
