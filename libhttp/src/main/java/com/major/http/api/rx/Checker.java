package com.major.http.api.rx;

/**
 * 作者:meijie
 * 包名:com.major.http.api.rx
 * 工程名:Common
 * 时间:2018/6/26 11:55
 * 说明:
 */
public interface Checker {
    /**
     * 判断是否业务成功
     *
     * @param code
     * @return
     */
    boolean isSuccess(int code);
}
