package com.major.http.api.interceptor;

import com.major.base.log.LogUtil;
import com.major.extra.util.REUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * TODO
 * Created by MEI on 2017/3/31.
 */
public abstract class SignInterceptor implements Interceptor {

    private Map<String, String> mParams = new TreeMap<>();

    /**
     * @param baseParams 基础参数
     */
    public SignInterceptor(Map<String, String> baseParams) {
        if (baseParams != null) {
            mParams.putAll(baseParams);
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long timestamp = System.currentTimeMillis() / 1000; // s

        if ("POST".equalsIgnoreCase(request.method())) {
            RequestBody requestBody = request.body();
            if (requestBody instanceof MultipartBody) {
                mParams.putAll(GetParms(request));
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                LogUtil.i("--params-->" + mParams.entrySet());
                for (Map.Entry<String, String> entry : mParams.entrySet()) {
                    LogUtil.i("body 中参数 " + entry.getKey() + ", " + entry.getValue());
//                    builder.addFormDataPart(entry.getKey(), "", parseRequestBody(entry.getValue()));
                    builder.addFormDataPart(entry.getKey(), entry.getValue());
                }
                MultipartBody resbody = (MultipartBody) request.body();
                int size = resbody.size();
                for (int i = 0; i < size; i++) {
                    MultipartBody.Part part = resbody.part(i);
                    LogUtil.i("--part-->" + part.toString());
                    builder.addPart(part);
                }

                MultipartBody body = builder.build();
                HttpUrl rawHttpUrl = request.url();
                request = request.newBuilder()
                        .url(rawHttpUrl)
                        .post(body)
                        .build();
                LogUtil.i("POST 请求(MultipartBody) URL " + rawHttpUrl);
            } else {
                mParams.putAll(parsePost(request));
                FormBody.Builder builder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : mParams.entrySet()) {
                    builder.add(entry.getKey(), entry.getValue());
                    //                LogUtil.i("body 中参数 " + entry.getKey() + ", " + entry.getValue());
                }
                FormBody body = builder.build();
                HttpUrl rawHttpUrl = request.url();
                request = request.newBuilder()
                        .url(rawHttpUrl)
                        .post(body)
                        .build();

                LogUtil.i("POST 请求 URL: " + rawHttpUrl);
            }

            // 添加签名
            request = request.newBuilder()
                    .addHeader("sign", encryptStr(timestamp, mParams))
                    .build();

            printBodyStr(request);
        } else if ("GET".equalsIgnoreCase(request.method())) {
            HttpUrl rawHttpUrl = request.url();
            HttpUrl.Builder builder = rawHttpUrl.newBuilder();

            // 添加基础参数
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }

            HttpUrl newUrl = builder.build();
            request = request.newBuilder()
                    .url(newUrl)
                    .build();

            // 获取参数对
            HttpUrl httpURlWithParams = request.url();
            Set<String> strings = httpURlWithParams.queryParameterNames();
            Map<String, String> params1 = new TreeMap<>();
            for (String next : strings) {
                String val = httpURlWithParams.queryParameter(next);
                params1.put(next, val);
                if ("n".equalsIgnoreCase(next) && "1".equalsIgnoreCase(val)) {
                    LogUtil.e("未签名校验");
                }
            }
            LogUtil.i("GET 请求 URL: " + newUrl.toString());
            request = request.newBuilder()
                    .addHeader("sign", encryptStr(timestamp, params1))
                    .build();
        }

        return chain.proceed(request);
    }

    // 加密字符串
    public abstract String encryptStr(long timestamp, Map<String, String> params);


    // 查看 post 参数
    private void printBodyStr(Request request) throws IOException {
        Buffer bs = new Buffer();
        request.body().writeTo(bs);
        String s = bs.readUtf8();
        LogUtil.i("printBodyStr s = " + s);
    }

    /**
     * 获取表单中的参数
     *
     * @param request
     * @return
     */
    private Map<? extends String, ? extends String> GetParms(Request request) {
        TreeMap<String, String> map = null;
        Buffer bs_old = new Buffer();
        try {
            request.body().writeTo(bs_old);
            String str = bs_old.readUtf8();
            if (str.isEmpty()) {
                return map;
            }
            map = new TreeMap<>();
            String[] split = str.split("--");
            for (String s : split) {
                LogUtil.i("GetParms body s = " + s);
                String[] split1 = s.split("\n");
                if (split1.length > 2) {
                    String Key = split1[1];
                    String[] splitKey = Key.split("=");
                    if (splitKey.length == 2) {
                        String key = splitKey[1].replace("\"", "").replace("\r", "");
                        LogUtil.i("GetParms body concrectkey " + key + "--value--" + split1[split1.length - 1]);
                        if (!REUtil.isSpecialChar(key) && !key.equals("file")) {
                            map.put(key, split1[split1.length - 1].replace("\r", "").replace("\"", ""));
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return map;
    }

    // 获取 post 参数
    private Map<String, String> parsePost(Request request) throws IOException {

        Buffer bs_old = new Buffer();
        request.body().writeTo(bs_old);
        String str = bs_old.readUtf8();
        LogUtil.i("body bs_old = " + str);

        Map<String, String> map = new HashMap<>();

        if (str.isEmpty()) {
            return map;
        }
        String[] split = str.split("&");
        for (String s : split) {
            String[] keyVal = s.split("=");
            if (keyVal.length == 2) {
                map.put(keyVal[0], keyVal[1]);
            }
        }

        return map;
    }
}
