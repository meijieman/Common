package com.hongfans.common.rx;

import com.hongfans.common.api.exception.ApiException;

import rx.Subscriber;

/**
 * TODO
 * Created by MEI on 2017/4/7.
 */
public abstract class RxSubscriber<T> extends Subscriber<T> {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            onError(new ApiException(e));
        }
    }

    public abstract void onError(ApiException e);
}
