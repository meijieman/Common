package com.major.http.api.rx;

import com.major.http.api.exception.ApiException;

import rx.Subscriber;

/**
 * TODO
 * Created by MEI on 2017/4/7.
 */
public abstract class RxSubscriber<T> extends Subscriber<T>{

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onCompleted(){

    }

    @Override
    public void onError(Throwable e){
        ApiException ex;
        if(e instanceof ApiException){
            ex = (ApiException) e;
        } else {
            ex = new ApiException(e);
        }
        onError(ex.getDesc(), ex.getCode());
    }

    /**
     * 返回的错误回调
     *
     * @param err
     * @param code
     */
    public abstract void onError(String err, int code);
}
