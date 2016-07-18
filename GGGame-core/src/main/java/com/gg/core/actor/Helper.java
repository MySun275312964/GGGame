package com.gg.core.actor;

import com.gg.common.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public class Helper {
    private static Map<Class<?>, String> classNameMap = new ConcurrentHashMap<>();

    public static String getMethodDesc(Method method) {
        int plen = method.getParameterCount();
        if (plen < 0) {
            return "";
        }

        Class<?>[] params = method.getParameterTypes();
        String[] strs = new String[plen + 1];
        strs[0] = method.getName();
        for (int i = 1; i <= plen; i++) {
            strs[i] = getClassName(params[i-1]);
        }

        return StringUtils.join(":", strs);
    }

    private static String getClassNameWithoutCache(Class<?> clz) {
        if (!clz.isArray()) {
            return clz.getName();
        }

        StringBuilder sb = new StringBuilder();
        while (clz.isArray()) {
            sb.append("[]");
            clz = clz.getComponentType();
        }

        return clz.getName() + sb.toString();
    }

    public static String getClassName(Class<?> clz) {
        if (clz == null) {
            return null;
        }

        String className = classNameMap.get(clz);

        if (className != null) {
            return className;
        }

        className = getClassNameWithoutCache(clz);

        classNameMap.putIfAbsent(clz, className);

        return className;
    }
}
