package com.hongfans.common.rx;

import com.hongfans.common.api.exception.ApiException;

import rx.Observable;
import rx.functions.Func1;

/**
 * TODO
 * Created by MEI on 2017/4/18.
 */
public class RxErr<R> implements Func1<Throwable, Observable<R>> {

    @Override
    public Observable<R> call(Throwable throwable) {
        return Observable.error(ApiException.handleException(throwable));
    }
}