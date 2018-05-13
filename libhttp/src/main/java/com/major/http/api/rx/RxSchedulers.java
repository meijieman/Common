package com.major.http.api.rx;

import com.major.http.api.ResponseBean;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @desc: TODO
 * @author: Major
 * @since: 2017/2/22 1:02
 */
public class RxSchedulers {

    public static <T> Observable.Transformer<T, T> switchThird() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorResumeNext(new RxErr<T>());
            }
        };
    }

    public static <T> Observable.Transformer<ResponseBean<T>, T> combine() {
        return new Observable.Transformer<ResponseBean<T>, T>() {
            @Override
            public Observable<T> call(Observable<ResponseBean<T>> responseObservable) {
                return responseObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new RxMapFunc1<T>() {
                            @Override
                            boolean isSuccess(int code) {
                                // FIXME: 2018/1/16
                                return true;
                            }
                        })
                        .onErrorResumeNext(new RxErr<T>());
            }
        };
    }

    public static <T> Observable.Transformer<ResponseBean<T>, ResponseBean<T>> combine2() {
        return new Observable.Transformer<ResponseBean<T>, ResponseBean<T>>() {
            @Override
            public Observable<ResponseBean<T>> call(Observable<ResponseBean<T>> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new RxMapFunc2<T>() {
                            @Override
                            protected boolean isSuccess(int code) {
                                // FIXME: 2018/1/16
                                return true;
                            }
                        })
                        .onErrorResumeNext(new RxErr<ResponseBean<T>>());
            }
        };
    }
}
