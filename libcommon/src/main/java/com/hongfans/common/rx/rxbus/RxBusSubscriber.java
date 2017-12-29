package com.hongfans.common.rx.rxbus;

import rx.Subscriber;

/**
 * TODO
 * Created by MEI on 2017/7/14.
 */
public abstract class RxBusSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
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

    protected abstract void onEvent(T t);
}
