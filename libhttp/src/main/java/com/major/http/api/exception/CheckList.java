package com.major.http.api.exception;

import android.util.SparseArray;

/**
 * 作者:meijie
 * 包名:com.major.http.api.exception
 * 工程名:Common
 * 时间:2018/7/24 11:59
 * 说明: 错误码对照表
 */
public class CheckList {

    //对应HTTP的状态码
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    public static final int ERROR_UNKNOWN = -1; // 未知异常

    // 网络异常 1010-1030
    public static final int ERROR_NETWORK_CONNECT = 1010;
    public static final int ERROR_NETWORK_SOCKET_TIMEOUT = 1011;
    public static final int ERROR_NETWORK_SOCKET = 1012;
    public static final int ERROR_NETWORK_UNKNOWN_HOST = 1013;

    public static final int ERROR_URL = 1035;
    public static final int ERROR_PAUSE = 1038;


    private static String MSG_NET_ERROR = "网络异常，请检查网络后重试。";
    private static String MSG_SERVER_ERROR = "服务器异常，请稍后重试。";
    private static String MSG_SERVER_ERROR_1 = "服务器响应超时，请稍后重试。";

    private static SparseArray<String> mErrors = new SparseArray<>();


    static {
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

        // 网络错误 1010 - 1030
        mErrors.put(ERROR_NETWORK_CONNECT, MSG_NET_ERROR); // 请求链接超时
        mErrors.put(ERROR_NETWORK_SOCKET_TIMEOUT, MSG_SERVER_ERROR_1); // 服务结果返回超时
        mErrors.put(ERROR_NETWORK_SOCKET, MSG_NET_ERROR);
        mErrors.put(ERROR_NETWORK_UNKNOWN_HOST, MSG_NET_ERROR);

        // 逻辑错误
        mErrors.put(ERROR_UNKNOWN, "未知异常");
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

    public static void add(int code, String desc) {
        mErrors.put(code, desc);
    }

    public static boolean contain(int code) {
        return mErrors.get(code) != null;
    }

    public static String get(int code) {
        return mErrors.get(code, "未定义异常码 " + code);
    }

    public static ApiException assemble(int code) {
        return new ApiException(code, mErrors.get(code, "the code " + code + " desc not found"));
    }
}
