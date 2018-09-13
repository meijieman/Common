package com.major.base.rx.rxbus;

/**
 * TODO
 * Created by MEI on 2017/7/14.
 *
 * @deprecated 升级 RxJava2 后不可用，使用
 * {@link RxBusObserver}
 */
public abstract class RxBusSubscriber<T> /*extends Subscriber<T>*/ {

   /* @Override
    public void onComplete() {

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

    protected abstract void onEvent(T t);*/
}
