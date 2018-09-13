package com.major.http.api.rx;

import com.major.http.api.exception.ApiException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 作者:meijie
 * 包名:com.major.http.api.rx
 * 工程名:Common
 * 时间:2018/9/11 16:56
 * 说明:
 */
public abstract class RxObserver<T> implements Observer<T> {


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
        ApiException ex;
        if (e instanceof ApiException) {
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

    @Override
    public void onComplete() {

    }
}
