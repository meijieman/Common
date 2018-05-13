package com.major.http.api.rx;

import com.major.base.log.LogUtil;
import com.major.http.api.ResponseBean;
import com.major.http.api.exception.ApiException;
import com.major.http.api.exception.ECPair;

import rx.functions.Func1;

/**
 * TODO
 * Created by MEI on 2017/4/19.
 */
abstract class RxMapFunc1<F> implements Func1<ResponseBean<F>, F> {

    @Override
    public F call(ResponseBean<F> f) {
        LogUtil.i(ApiException.TAG_API_EXCEPTION, "RxMapFunc1 " + f);
        int code = f.getCode();
        if (isSuccess(code)) {
            return f.getData();
        } else {
            throw new ApiException(new ECPair.ECPairImpl(f.getCode(), f.getMsg()));
        }
    }

    abstract boolean isSuccess(int code);

}
