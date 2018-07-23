package com.major.http.api.exception;

import android.util.SparseArray;

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
public class ApiException extends RuntimeException{

    public static final String TAG_API_EXCEPTION = "tag_api_exception";

    // 响应码
    private int code;
    // 状态描述
    private String desc;

    public int getCode(){
        return code;
    }

    public void setCode(int code){
        this.code = code;
    }

    public String getDesc(){
        return desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    private static HandleException sHandleException;

    /**
     * 设置自行拦截的异常
     *
     * @param exception
     */
    public static void setHandle(HandleException exception){
        sHandleException = exception;
    }

    public ApiException(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public ApiException(Throwable throwable){
        super(throwable);
        ApiException e;
        if(throwable instanceof ApiException){
            e = (ApiException) throwable;
        } else {
            e = CheckList.assemble(CheckList.ERROR_UNKNOWN);
        }
        code = e.code;
        desc = e.desc;
    }

    public static ApiException handleException(Throwable e){
        LogUtil.e(TAG_API_EXCEPTION, "handleException " + e);
        if(e instanceof HttpException){
            //HTTP错误
            HttpException httpEx = (HttpException) e;
            if(CheckList.contain(httpEx.code())){
                return CheckList.assemble(httpEx.code());
            } else {
                // 未定义 http 异常
                return new ApiException(httpEx.code(), "未定义 http 异常");
            }
        } else if(e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof MalformedJsonException
                || e instanceof ParseException){
            return CheckList.assemble(CheckList.ERROR_PAUSE);
        } else if(e instanceof ConnectException){
            // 一次通讯交互中，如果请求失败，说明未能成功请求到服务器，可以允许用户再次提交。
            return CheckList.assemble(CheckList.ERROR_NETWORK_CONNECT);
        } else if(e instanceof SocketTimeoutException){
            // 如果是响应失败，就说明用户提交是成功了的
            return CheckList.assemble(CheckList.ERROR_NETWORK_SOCKET_TIMEOUT);
        } else if(e instanceof SocketException){
            return CheckList.assemble(CheckList.ERROR_NETWORK_SOCKET);
        } else if(e instanceof UnknownHostException){
            return CheckList.assemble(CheckList.ERROR_NETWORK_UNKNOWN_HOST);
        } else if(e instanceof MalformedURLException){
            return CheckList.assemble(CheckList.ERROR_URL);
        } else if(e instanceof ApiException){
            //服务器返回的业务错误，如 token 失效
            ApiException ex = (ApiException) e;
            if(sHandleException != null){
                // 自行处理异常
                ApiException handle = sHandleException.handle(ex);
                if(handle == null){
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

    @Override
    public String toString(){
        return "ApiException{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }

    /**
     * 自定义处理异常
     */
    public interface HandleException{

        /**
         * 自行处理异常
         *
         * @param e
         * @return
         */
        ApiException handle(ApiException e);
    }

    /**
     * 错误码对照表
     */
    static class CheckList{

        //对应HTTP的状态码
        static final int UNAUTHORIZED = 401;
        static final int FORBIDDEN = 403;
        static final int NOT_FOUND = 404;
        static final int REQUEST_TIMEOUT = 408;
        static final int INTERNAL_SERVER_ERROR = 500;
        static final int BAD_GATEWAY = 502;
        static final int SERVICE_UNAVAILABLE = 503;
        static final int GATEWAY_TIMEOUT = 504;

        // 未知异常
        static final int ERROR_UNKNOWN = -1;

        static final int ERROR_NETWORK_CONNECT = 1010;
        public static final int ERROR_NETWORK_SOCKET_TIMEOUT = 1011;
        public static final int ERROR_NETWORK_SOCKET = 1012;
        public static final int ERROR_NETWORK_UNKNOWN_HOST = 1013;

        static final int ERROR_URL = 1035;
        static final int ERROR_PAUSE = 1038;


        private static String MSG_NET_ERROR = "网络异常，请检查网络后重试。";
        private static String MSG_SERVER_ERROR = "服务器异常，请稍后重试。";
        private static String MSG_SERVER_ERROR_1 = "服务器响应超时，请稍后重试。";

        private static SparseArray<String> mErrors = new SparseArray<>();


        {
            // 正常
            mErrors.put(200, "服务器响应正常");

            // http 异常
            mErrors.put(UNAUTHORIZED, MSG_SERVER_ERROR);
            mErrors.put(FORBIDDEN, MSG_SERVER_ERROR);
            mErrors.put(NOT_FOUND, MSG_SERVER_ERROR);
            mErrors.put(REQUEST_TIMEOUT, MSG_NET_ERROR);
            mErrors.put(INTERNAL_SERVER_ERROR, MSG_SERVER_ERROR);
            mErrors.put(BAD_GATEWAY, MSG_SERVER_ERROR);
            mErrors.put(SERVICE_UNAVAILABLE, MSG_SERVER_ERROR);
            mErrors.put(GATEWAY_TIMEOUT, MSG_NET_ERROR);

            mErrors.put(ERROR_UNKNOWN, "未知异常");

            // 网络错误 1010 - 1030
            mErrors.put(ERROR_NETWORK_CONNECT, MSG_NET_ERROR); // 请求链接超时
            mErrors.put(ERROR_NETWORK_SOCKET_TIMEOUT, MSG_SERVER_ERROR_1); // 服务结果返回超时
            mErrors.put(ERROR_NETWORK_SOCKET, MSG_NET_ERROR);
            mErrors.put(ERROR_NETWORK_UNKNOWN_HOST, MSG_NET_ERROR);


            // 逻辑错误
            mErrors.put(1030, "开发者签名未通过");
            mErrors.put(1031, "没有权限使用相应的接口");
            mErrors.put(1032, "请求参数非法");
            mErrors.put(1033, "请求条件中，缺少必填参数");
            mErrors.put(1034, "服务端未知错误");
            mErrors.put(1035, "url异常"); // MalformedURLException	为java抛出的异常，SDK出现概率极低
            mErrors.put(1036, "初始化异常，请先调用 init()");
            mErrors.put(1037, "初始化异常，请检查 appKey/appSecret");
            mErrors.put(ERROR_PAUSE, "协议解析错误");
            mErrors.put(1039, "未知错误"); // SDK 本地错误码
            // IO 操作异常 - IOException    SDK本地错误码
            // 空指针异常 - NullPointException	SDK本地错误码


        }

        public static void add(int code, String desc){
            mErrors.put(code, desc);
        }

        public static boolean contain(int code){
            return mErrors.get(code) != null;
        }

        public static String get(int code){
            return mErrors.get(code, "未定义异常码 " + code);
        }

        public static ApiException assemble(int code){
            return new ApiException(code, mErrors.get(code, "the code " + code + " desc not found"));
        }
    }
}
