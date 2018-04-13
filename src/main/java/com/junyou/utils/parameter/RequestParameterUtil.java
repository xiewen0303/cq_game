/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.utils.parameter;

/**
 *@Description 客户端请求参数解析工具类
 *@Author Yang Gao
 *@Since 2016-7-19
 *@Version 1.1.0
 */
public class RequestParameterUtil {

    
    public static String object2String(Object obj) {
        if (null == obj)
            return null;
        if (obj instanceof String) {
            return (String)obj;
        }
        return null;
    }
    public static Byte object2Byte(Object obj) {
        if (null == obj)
            return null;
        if (obj instanceof Byte) {
            return (Byte) obj;
        }
        return null;
    }
    
    public static Short object2Short(Object obj) {
        if (null == obj)
            return null;
        if (obj instanceof Short) {
            return (Short) obj;
        }
        return null;
    }
    
    public static Integer object2Integer(Object obj) {
        if (null == obj)
            return null;
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        return null;
    }
    
    public static Long object2Long(Object obj) {
        if (null == obj)
            return null;
        if (obj instanceof Long) {
            return (Long) obj;
        }
        return null;
    }
    
    public static Double object2Double(Object obj) {
        if (null == obj)
            return null;
        if (obj instanceof Double) {
            return (Double) obj;
        }
        return null;
    }
    
    public static Float object2Float(Object obj) {
        if (null == obj)
            return null;
        if (obj instanceof Float) {
            return (Float) obj;
        }
        return null;
    }
    
    public static Boolean object2Boolean(Object obj) {
        if (null == obj)
            return null;
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        return null;
    }
    
    
}
