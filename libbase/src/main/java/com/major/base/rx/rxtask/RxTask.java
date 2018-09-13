package com.major.base.rx.rxtask;

import android.util.SparseArray;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava 线程切换替代 Thread 和 Handler
 * Created by MEI on 2017/8/2.
 * <p/>
 * http://shiyiliang.cn/2017/07/09/RxJava%E7%BA%BF%E7%A8%8B%E5%88%87%E6%8D%A2%E4%BB%A3%E6%9B%BFThread%E5%92%8CHandler/
 */

public class RxTask {

    private static SparseArray<Disposable> mArray = new SparseArray<>();


    public static Disposable doOnIOThread(IOTask task) {
        return doOnIOThreadDelay(task, 0, TimeUnit.MILLISECONDS);
    }

    public static Disposable doOnIOThreadDelay(IOTask task, long delay, TimeUnit unit) {
        return Observable.just(task)
                .observeOn(Schedulers.io())
                .delay(delay, unit)
                .subscribe(ioTask -> ioTask.onIOThread(),
                        throwable -> throwable.printStackTrace());
    }

    public static Disposable doOnUIThread(UITask task) {
        return doOnUIThreadDelay(task, -1, 0, TimeUnit.MILLISECONDS);
    }

    public static Disposable doOnUIThreadDelay(UITask task, long delay, TimeUnit unit) {
        return doOnUIThreadDelay(task, -1, delay, unit);
    }

    public static Disposable doOnUIThreadDelay(UITask task, int tag, long delay, TimeUnit unit) {
        final Disposable disposable;
        disposable = Observable.just(task)
                .delay(delay, unit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uiTask -> uiTask.onUIThread(),
                        throwable -> throwable.printStackTrace());
        if (tag != -1) {
            mArray.put(tag, disposable);
        }
        return disposable;
    }

    public static <T> Disposable doTask(final Task<T> task) {
        return Observable
                .create((ObservableOnSubscribe<T>) emitter -> {
                    T t = task.onIOThread();
                    emitter.onNext(t);
                    emitter.onComplete();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(t -> task.onUIThread(t),
                        throwable -> throwable.printStackTrace());
    }

    public static void cancel(int tag) {
        Disposable disposable = mArray.get(tag);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            mArray.remove(tag);
        }
    }

    public static void cancelAll() {
        int size = mArray.size();
        for (int i = 0; i < size; i++) {
            Disposable disposable = mArray.get(mArray.keyAt(i));
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        mArray.clear();
    }

    public interface IOTask {

        void onIOThread();
    }

    public interface UITask {

        void onUIThread();
    }

    public interface Task<T> {

        T onIOThread();

        void onUIThread(T t);
    }
}
