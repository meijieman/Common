package com.hongfans.common.mvp;

import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * * @author soul
 *
 * @项目名:Compilations
 * @包名: com.soul.library.base
 * @作者：祝明
 * @描述：TODO
 * @创建时间：2017/1/11 22:53
 */

public class BasePresenter<V extends IView, M extends IModel> implements IPresenter<V> {

    protected M mModel;
    protected V mView;

    private CompositeSubscription mCompositeSubscription;
    private WeakReference<V> mActReference;

    @Override
    public void attachView(V view) {
        mActReference = new WeakReference<>(view);
        mView = mActReference.get();
    }

    @Override
    public void detachView() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
        if (mActReference != null) {
            mActReference.clear();
            mActReference = null;
        }
    }

    public void addSubscription(Subscription s) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(s);
    }
}
