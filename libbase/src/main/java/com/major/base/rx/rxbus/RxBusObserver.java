package com.major.base.rx.rxbus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 作者:meijie
 * 包名:com.major.base.rx.rxbus
 * 工程名:Common
 * 时间:2018/9/11 17:04
 * 说明:
 */
public abstract class RxBusObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        // 对 next 事件进行 try-catch
        try {
            onEvent(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onEvent(T t);

}
