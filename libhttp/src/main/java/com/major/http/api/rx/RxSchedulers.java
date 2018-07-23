package com.major.http.api.rx;

import com.major.http.api.exception.ApiException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @desc: TODO
 * @author: Major
 * @since: 2017/2/22 1:02
 */
public class RxSchedulers {

    private static Checker sChecker = new Checker(){
        @Override
        public boolean isSuccess(int code){
            return true;
        }
    };

    public static void setChecker(Checker checker){
        sChecker = checker;
    }

    public static <T> Observable.Transformer<RxResp<T>, T> combine(){
        return new Observable.Transformer<RxResp<T>, T>(){
            @Override
            public Observable<T> call(Observable<RxResp<T>> responseObservable){
                return responseObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new RxMapFunc1<T>(sChecker))
                        .onErrorResumeNext(new RxErr<T>());
            }
        };
    }

    public static <T> Observable.Transformer<RxResp<T>, RxResp<T>> combine2(){
        return new Observable.Transformer<RxResp<T>, RxResp<T>>(){
            @Override
            public Observable<RxResp<T>> call(Observable<RxResp<T>> observable){
                return observable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new RxMapFunc2<T>(sChecker))
                        .onErrorResumeNext(new RxErr<RxResp<T>>());
            }
        };
    }

    public static <T> Observable.Transformer<T, T> switchThird(){
        return new Observable.Transformer<T, T>(){
            @Override
            public Observable<T> call(Observable<T> tObservable){
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorResumeNext(new RxErr<T>());
            }
        };
    }

    static class RxErr<R> implements Func1<Throwable, Observable<R>>{

        @Override
        public Observable<R> call(Throwable throwable) {
            return Observable.error(ApiException.handleException(throwable));
        }
    }
}
