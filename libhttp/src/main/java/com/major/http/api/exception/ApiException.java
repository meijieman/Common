package com.major.http.api.exception;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;
import com.major.base.log.LogUtil;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * TODO
 * Created by MEI on 2017/4/18.
 */
public class ApiException extends RuntimeException {

    public static final String TAG_API_EXCEPTION = "tag_api_exception";

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    private ECPair mECPair;


    public ApiException(ECPair ecPair) {
        mECPair = ecPair;
    }

    public ApiException(Throwable throwable) {
        super(throwable);
    }

    public ECPair getECPair(){
        return mECPair;
    }

    public static ApiException handleException(Throwable e) {
        LogUtil.e(TAG_API_EXCEPTION, "handleException " + e);
        if (e instanceof HttpException) {
            //HTTP错误
            HttpException httpEx = (HttpException) e;
            switch (httpEx.code()) {
                case REQUEST_TIMEOUT:
                    return new ApiException(ECPair.HTTP_REQUEST_TIMEOUT);
                case GATEWAY_TIMEOUT:
                    return new ApiException(ECPair.HTTP_GATEWAY_TIMEOUT);
                case UNAUTHORIZED:
                    return new ApiException(ECPair.HTTP_UNAUTHORIZED);
                case FORBIDDEN:
                    return new ApiException(ECPair.HTTP_FORBIDDEN);
                case NOT_FOUND:
                    return new ApiException(ECPair.HTTP_NOT_FOUND);
                case INTERNAL_SERVER_ERROR:
                    return new ApiException(ECPair.HTTP_INTERNAL_SERVER_ERROR);
                case BAD_GATEWAY:
                    return new ApiException(ECPair.HTTP_BAD_GATEWAY);
                case SERVICE_UNAVAILABLE:
                    return new ApiException(ECPair.HTTP_SERVICE_UNAVAILABLE);
                default:
                    // 未定义 http 异常
                    return new ApiException(new ECPair.ECPairImpl(httpEx.code(), ECPair.HTTP_UNKNOW.getDesc()));
            }
        } else if (e instanceof ApiException) {
            //服务器返回的错误
            // 4019 token 失效，统一处理
            if (ECPair.TOKEN_INVALID.equals(((ApiException) e).getECPair())) {
                return new ApiException(ECPair.TOKEN_INVALID);
            }
            return (ApiException) e;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof MalformedJsonException
                || e instanceof ParseException) {
            return new ApiException(ECPair.ERROR_PARSE);
        } else if (e instanceof ConnectException) {
            return new ApiException(ECPair.ERROR_NETWORK_CONNECT);
        } else if (e instanceof SocketTimeoutException) {
            return new ApiException(ECPair.ERROR_NETWORK_SOCKET_TIMEOUT);
        } else if (e instanceof SocketException) {
            return new ApiException(ECPair.ERROR_NETWORK_SOCKET);
        } else if (e instanceof UnknownHostException) {
            return new ApiException(ECPair.ERROR_NETWORK_UNKNOWN_HOST);
        } else if (e instanceof MalformedURLException) {
            return new ApiException(ECPair.ERROR_URL);
        } else {
            String desc = e == null ? ECPair.ERROR_UNKNOWN.getDesc() : e.toString();
            LogUtil.e(desc);
            return new ApiException(new ECPair.ECPairImpl(ECPair.ERROR_UNKNOWN.getCode(), desc));
        }
    }

    @Override
    public String toString() {
        return "ApiException{" +
                "mECPair=" + mECPair +
                '}';
    }
}
