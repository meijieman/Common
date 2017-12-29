package com.hongfans.common1.util;

import java.util.Collection;
import java.util.Map;

/**
 * 辅助判断
 */
public class CheckUtil{

    public static boolean isEmpty(CharSequence str){
        return isNull(str) || str.length() == 0;
    }

    public static boolean isNull(Object o){
        return o == null;
    }

    public static boolean isEmpty(Object[] os){
        return isNull(os) || os.length == 0;
    }

    public static boolean isEmpty(Collection<?> l){
        return isNull(l) || l.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> m){
        return isNull(m) || m.isEmpty();
    }
}
