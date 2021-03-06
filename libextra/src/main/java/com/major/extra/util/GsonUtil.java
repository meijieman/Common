package com.major.extra.util;

import com.google.gson.Gson;
import com.major.base.log.LogUtil;

import java.lang.reflect.Type;

/**
 * TODO
 * Created by MEI on 2017/4/6.
 */
public class GsonUtil {

    private static Gson mGson = new Gson();

    public static String toJson(Object obj) {
        return mGson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return mGson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return mGson.fromJson(json, typeOfT);
    }

    /**
     * M 转为 T
     */
    public static <M, T> T copy(M model, Class<T> clazz) {
        LogUtil.i("copy " + model.getClass().getSimpleName() + " --> " + clazz.getSimpleName() + "，生成的 json " + toJson(model));
        T t = fromJson(toJson(model), clazz);
        LogUtil.i("copy 生成 " + t);
        return t;
    }

    public static <M, T> T copy(M model, Type clazz) {
        LogUtil.i("copy " + "，生成的 json " + toJson(model));
        T t = fromJson(toJson(model), clazz);
        LogUtil.i("copy 生成 " + t);
        return t;
    }
}
