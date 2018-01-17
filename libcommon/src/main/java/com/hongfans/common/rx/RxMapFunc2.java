package com.hongfans.common.rx;

import com.hongfans.common.api.ResponseBean;
import com.hongfans.common.api.exception.ApiException;
import com.hongfans.common.api.exception.ECPair;
import com.hongfans.common.log.LogUtil;

import rx.functions.Func1;

/**
 * TODO
 * Created by MEI on 2017/4/19.
 */
abstract class RxMapFunc2<F> implements Func1<ResponseBean<F>, ResponseBean<F>> {

    @Override
    public ResponseBean<F> call(ResponseBean<F> f) {
        LogUtil.i(ApiException.TAG_API_EXCEPTION, "RxMapFunc2 " + f);
        int code = f.getCode();
        if (isSuccess(code)) {
            return f;
        } else {
            throw new ApiException(new ECPair.ECPairImpl(f.getCode(), f.getMsg()));
        }
    }

    protected abstract boolean isSuccess(int code);
}
