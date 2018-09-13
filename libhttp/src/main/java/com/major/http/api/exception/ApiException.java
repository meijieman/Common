package com.major.http.api.exception;

import android.util.MalformedJsonException;

import com.major.base.log.LogUtil;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * TODO
 * Created by MEI on 2017/4/18.
 */
public class ApiException extends RuntimeException {

    public static final String TAG_API_EXCEPTION = "tag_api_exception";
    private static HandleException sHandleException;
    // 响应码
    private int code;
    // 状态描述
    private String desc;

    public ApiException(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public ApiException(Throwable throwable) {
        super(throwable);
        ApiException e;
        if (throwable instanceof ApiException) {
            e = (ApiException) throwable;
        } else {
            e = CheckList.assemble(CheckList.ERROR_UNKNOWN);
        }
        code = e.code;
        desc = e.desc;
    }

    /**
     * 设置自行拦截的异常
     *
     * @param exception
     */
    public static void setHandle(HandleException exception) {
        sHandleException = exception;
    }

    public static ApiException handleException(Throwable e) {
        LogUtil.e(TAG_API_EXCEPTION, "handleException " + e);
        if (e instanceof HttpException) {
            //HTTP错误
            HttpException httpEx = (HttpException) e;
            if (CheckList.contain(httpEx.code())) {
                return CheckList.assemble(httpEx.code());
            } else {
                // 未定义 http 异常
                return new ApiException(httpEx.code(), "未定义 http 异常");
            }
        } else if (e instanceof JSONException
                || e instanceof MalformedJsonException
                || e instanceof ParseException) {
            return CheckList.assemble(CheckList.ERROR_PAUSE);
        } else if (e instanceof ConnectException) {
            // 一次通讯交互中，如果请求失败，说明未能成功请求到服务器，可以允许用户再次提交。
            return CheckList.assemble(CheckList.ERROR_NETWORK_CONNECT);
        } else if (e instanceof SocketTimeoutException) {
            // 如果是响应失败，就说明用户提交是成功了的
            return CheckList.assemble(CheckList.ERROR_NETWORK_SOCKET_TIMEOUT);
        } else if (e instanceof SocketException) {
            return CheckList.assemble(CheckList.ERROR_NETWORK_SOCKET);
        } else if (e instanceof UnknownHostException) {
            return CheckList.assemble(CheckList.ERROR_NETWORK_UNKNOWN_HOST);
        } else if (e instanceof MalformedURLException) {
            return CheckList.assemble(CheckList.ERROR_URL);
        } else if (e instanceof ApiException) {
            //服务器返回的业务错误，如 token 失效
            ApiException ex = (ApiException) e;
            if (sHandleException != null) {
                // 自行处理异常
                ApiException handle = sHandleException.handle(ex);
                if (handle == null) {
                    throw new NullPointerException("自行处理的异常不能返回 null");
                }
                return handle;
            } else {
                return ex;
            }
        } else {
            String desc = e == null ? CheckList.get(CheckList.ERROR_UNKNOWN) : e.getMessage();
            return new ApiException(CheckList.ERROR_UNKNOWN, desc);
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ApiException{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }

    /**
     * 自定义处理异常
     */
    public interface HandleException {

        /**
         * 自行处理异常
         *
         * @param e
         * @return
         */
        ApiException handle(ApiException e);
    }
}
