package com.hongfans.common.api.exception;

/**
 * error code 错误码对照表
 * Created by MEI on 2017/4/19.
 */

public interface ECPair {

    int getCode();

    String getDesc();

    /*
       code==200为成功
       code >=4000 <=4999请求类型错误
       code >=5000 <=5999 服务器类型错误
    */

    ECPair RES_OK_0 = new ECPairImpl(0, "服务器响应正常"); // 服务调用正常，有结果返回（兼容以前 api）
    ECPair RES_OK = new ECPairImpl(200, "服务器响应正常"); // 服务调用正常，有结果返回
    ECPair TOKEN_INVALID = new ECPairImpl(4019, "token 失效");
    ECPair PAY_SUCCESS = new ECPairImpl(2017188, "资源支付成功"); //  资源支付成功

    // http 异常
    ECPair HTTP_UNAUTHORIZED = new ECPairImpl(401, ECPairImpl.MSG_SERVER_ERROR);
    ECPair HTTP_FORBIDDEN = new ECPairImpl(403, ECPairImpl.MSG_SERVER_ERROR);
    ECPair HTTP_NOT_FOUND = new ECPairImpl(404, ECPairImpl.MSG_SERVER_ERROR);
    ECPair HTTP_REQUEST_TIMEOUT = new ECPairImpl(408, ECPairImpl.MSG_NET_ERROR);
    ECPair HTTP_INTERNAL_SERVER_ERROR = new ECPairImpl(500, ECPairImpl.MSG_SERVER_ERROR);
    ECPair HTTP_BAD_GATEWAY = new ECPairImpl(502, ECPairImpl.MSG_SERVER_ERROR);
    ECPair HTTP_SERVICE_UNAVAILABLE = new ECPairImpl(503, ECPairImpl.MSG_SERVER_ERROR);
    ECPair HTTP_GATEWAY_TIMEOUT = new ECPairImpl(504, ECPairImpl.MSG_NET_ERROR);
    ECPair HTTP_UNKNOW = new ECPairImpl(-1, ECPairImpl.MSG_NET_ERROR);


    // 网络错误 1010 - 1030
    ECPair ERROR_NETWORK_CONNECT = new ECPairImpl(1010, ECPairImpl.MSG_NET_ERROR); // 请求链接超时
    ECPair ERROR_NETWORK_SOCKET_TIMEOUT = new ECPairImpl(1011, ECPairImpl.MSG_NET_ERROR); // 服务结果返回超时
    ECPair ERROR_NETWORK_SOCKET = new ECPairImpl(1012, ECPairImpl.MSG_NET_ERROR);
    ECPair ERROR_NETWORK_UNKNOWN_HOST = new ECPairImpl(1013, ECPairImpl.MSG_NET_ERROR);


    // 逻辑错误
    ECPair ERROR_INVALID_SIGN = new ECPairImpl(1030, "开发者签名未通过");
    ECPair ERROR_UNAUTHORIZED = new ECPairImpl(1031, "没有权限使用相应的接口");
    ECPair REQ_PARAM_INVALID = new ECPairImpl(1032, "请求参数非法");
    ECPair REQ_PARAM_LACK = new ECPairImpl(1033, "请求条件中，缺少必填参数");
    ECPair RES_UNKNOWN = new ECPairImpl(1034, "服务端未知错误");
    ECPair ERROR_URL = new ECPairImpl(1035, "url异常"); // MalformedURLException	为java抛出的异常，SDK出现概率极低
    ECPair ERROR_INIT = new ECPairImpl(1036, "初始化异常，请先调用 init()");
    ECPair ERROR_INIT_PARAM = new ECPairImpl(1037, "初始化异常，请检查 appKey/appSecret");
    ECPair ERROR_PARSE = new ECPairImpl(1038, "协议解析错误");
    ECPair ERROR_UNKNOWN = new ECPairImpl(1039, "未知错误"); // SDK 本地错误码
    // IO 操作异常 - IOException    SDK本地错误码
    // 空指针异常 - NullPointException	SDK本地错误码

    class ECPairImpl implements ECPair {

        private static String MSG_NET_ERROR = "网络异常，请检查网络后点击重试。";
        private static String MSG_SERVER_ERROR = "服务器异常，请稍后重试。";

        // 响应码
        private int code;
        // 状态描述
        private String desc;

        public ECPairImpl(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "ECPairImpl{" +
                    "code=" + code +
                    ", desc='" + desc + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ECPair) {
                if (code == ((ECPair) obj).getCode()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }
    }
}
