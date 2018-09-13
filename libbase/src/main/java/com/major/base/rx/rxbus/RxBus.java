package com.major.base.rx.rxbus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * 事件总线，类似 EventBus
 * <p/>
 * 参考 https://github.com/YoKeyword/RxBus
 * <p>
 * 2018年9月11日 14:13:45 rxjava1 替换为 rxjava2
 */
public class RxBus {

    private static volatile RxBus mDefaultInstance;

    private final FlowableProcessor<Object> mBus;
    private final Map<Class<?>, Object> mStickyEventMap;


    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    public RxBus() {
//        mBus = new SerializedSubject<>(PublishSubject.create());
        mBus = PublishProcessor.create().toSerialized(); // SerializedSubject 线程安全
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxBus getDefault() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }

    /**
     * 发送事件
     */
    public void post(Object o) {
        mBus.onNext(o);
    }

//    public <T> Observable<T> toObservable(Class<T> eventType) {
//        return mBus.ofType(eventType);
//        这里感谢小鄧子的提醒: ofType = filter + cast
//        return bus.filter(new Func1<Object, Boolean>() {
//            @Override
//            public Boolean call(Object o) {
//                return eventType.isInstance(o);
//            }
//        }) .cast(eventType);
//    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Flowable<T> toFlowable(Class<T> aClass) {
        return mBus.ofType(aClass);
    }

    /**
     * 判断是否有订阅者
     *
     * @return
     */
    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
    }

//    public boolean hasObservers() {
//        return mBus.hasObservers();
//    }

    public void reset() {
        mDefaultInstance = null;
    }

    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Flowable<T> toFlowableSticky(final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Flowable<T> flowable = mBus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);
            if (event != null) {
                return flowable.mergeWith(
                        Flowable.create(emitter -> emitter.onNext(eventType.cast(event)), BackpressureStrategy.BUFFER));
            } else {
                return flowable;
            }
        }
    }

//    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
//        synchronized (mStickyEventMap) {
//            Observable<T> observable = mBus.ofType(eventType);
//            final Object event = mStickyEventMap.get(eventType);
//            if (event != null) {
//                // 有 sticky 事件
//                return observable.mergeWith(Observable.create(new Observable.OnSubscribe<T>() {
//                    @Override
//                    public void call(Subscriber<? super T> subscriber) {
//                        subscriber.onNext(eventType.cast(event));
//                    }
//                }));
//            } else {
//                // 返回普通事件
//                return observable;
//            }
//        }
//    }

    /**
     * 根据eventType获取Sticky事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }
}