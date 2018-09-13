package com.major.http.api.rx;

import com.major.http.api.exception.ApiException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @desc: TODO
 * @author: Major
 * @since: 2017/2/22 1:02
 */
public class RxSchedulers {

    private static Checker sChecker = new Checker() {
        @Override
        public boolean isSuccess(int code) {
            return true;
        }
    };

    public static void setChecker(Checker checker) {
        sChecker = checker;
    }

    public static <T> ObservableTransformer<RxResp<T>, T> combine() {
        return new ObservableTransformer<RxResp<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<RxResp<T>> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new RxMapFunc1<T>(sChecker))
                        .onErrorResumeNext(new RxErr<T>());
            }
        };
    }

    public static <T> ObservableTransformer<RxResp<T>, RxResp<T>> combine2() {
        return new ObservableTransformer<RxResp<T>, RxResp<T>>() {
            @Override
            public Observable<RxResp<T>> apply(Observable<RxResp<T>> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new RxMapFunc2<T>(sChecker))
                        .onErrorResumeNext(new RxErr<RxResp<T>>());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> switchThird() {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorResumeNext(new RxErr<T>());
            }
        };
    }

    static class RxErr<R> implements Function<Throwable, Observable<R>> {

        @Override
        public Observable<R> apply(Throwable throwable) throws Exception {
            return Observable.error(ApiException.handleException(throwable));
        }
    }
}
