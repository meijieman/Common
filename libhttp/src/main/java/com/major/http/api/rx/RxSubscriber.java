package com.major.http.api.rx;

import com.major.http.api.exception.ApiException;
import com.major.http.api.exception.ECPair;

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
        ECPair ecPair;
        if(e instanceof ApiException){
            ecPair = ((ApiException) e).getECPair();
        } else {
            ecPair = ECPair.ECPairImpl.ERROR_UNKNOWN;
        }
        onError(ecPair.getDesc(), ecPair.getCode());
    }

    /**
     * 返回的错误回调
     *
     * @param err
     * @param code
     */
    public abstract void onError(String err, int code);
}
