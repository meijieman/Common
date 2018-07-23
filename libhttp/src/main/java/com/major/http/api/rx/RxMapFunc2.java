package com.major.http.api.rx;

import com.major.base.log.LogUtil;
import com.major.http.api.exception.ApiException;
import com.major.http.api.exception.ECPair;

import rx.functions.Func1;

/**
 * TODO
 * Created by MEI on 2017/4/19.
 */
final class RxMapFunc2<F> implements Func1<RxResp<F>, RxResp<F>> {

    private Checker mChecker;

    public RxMapFunc2(Checker checker) {
        if(checker == null){
            throw new NullPointerException("checker 不能为 null");
        }

        mChecker = checker;
    }

    @Override
    public RxResp<F> call(RxResp<F> f) {
        LogUtil.i(ApiException.TAG_API_EXCEPTION, "RxMapFunc2 " + f);
        int code = f.getCode();
        if (mChecker.isSuccess(code)) {
            return f;
        } else {
            throw new ApiException(new ECPair.ECPairImpl(f.getCode(), f.getMsg()));
        }
    }
}
