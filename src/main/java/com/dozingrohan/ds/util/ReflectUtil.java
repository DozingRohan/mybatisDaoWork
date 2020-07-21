package com.dozingrohan.ds.util;

import com.dozingrohan.ds.constant.DsConstant;

import java.util.HashSet;
import java.util.Set;

/**
 * DozingRohan
 * 2020/7/13
 * @Version: 1.0
 */
public class ReflectUtil {

    private static final Set<Class> DATABASE_SUPPORTED_TYPES;

    static{
        DATABASE_SUPPORTED_TYPES = new HashSet<>();
        DATABASE_SUPPORTED_TYPES.add(java.math.BigDecimal.class);
        DATABASE_SUPPORTED_TYPES.add(java.util.Date.class);
        DATABASE_SUPPORTED_TYPES.add(java.lang.Integer.class);
        DATABASE_SUPPORTED_TYPES.add(java.lang.Long.class);
        DATABASE_SUPPORTED_TYPES.add(java.lang.Float.class);
        DATABASE_SUPPORTED_TYPES.add(java.lang.Double.class);
    }

    /**
     * 是否支持 大于 小于 between 操作的类型
     * @param clazz
     * @return
     */
    public static boolean isUnSupportedComparableType(Class<?> clazz){
        return !DATABASE_SUPPORTED_TYPES.contains(clazz);
    }

    public static String getSetLessMethodName(String propertyName){
        char[] ch = propertyName.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new StringBuilder(DsConstant.LESS_SETTER).append(ch).toString();
    }

    public static String getSetGreaterMethodName(String propertyName){
        char[] ch = propertyName.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new StringBuilder(DsConstant.GREATER_SETTER).append(ch).toString();
    }


}
