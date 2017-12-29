package com.hongfans.common.router;

import android.support.v4.util.ArrayMap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2017/7/5.
 */

public class RouterBus {

    private static ArrayMap<Class,Object> mRouterMap = new ArrayMap<>();


    public static <T> T getRounter(Class<T> c) {
        T router = (T) mRouterMap.get(c);

        if (router == null){
            router = (T) Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return null;
                }
            });
            mRouterMap.put(c,router);
        }

        return router;
    }

}
