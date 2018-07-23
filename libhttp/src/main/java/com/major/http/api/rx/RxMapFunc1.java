package com.major.http.api.rx;

import com.major.base.log.LogUtil;
import com.major.http.api.exception.ApiException;

import rx.functions.Func1;

/**
 * TODO
 * Created by MEI on 2017/4/19.
 */
final class RxMapFunc1<F> implements Func1<RxResp<F>, F> {

    private Checker mChecker;

    public RxMapFunc1(Checker checker) {
        if(checker == null){
            throw new NullPointerException("checker 不能为 null");
        }
        mChecker = checker;
    }

    @Override
    public F call(RxResp<F> f) {
        LogUtil.i(ApiException.TAG_API_EXCEPTION, "RxMapFunc1 " + f);
        int code = f.getCode();
        if (mChecker.isSuccess(code)) {
            return f.getData();
        } else {
            throw new ApiException(f.getCode(), f.getMsg());
        }
    }
}
