package com.qian.system.common.utils;

/**
 * 类型转换器
 */
public class Convert {
    /**
     * 转换为字符串
     */
    public static String toStr(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

    /**
     * 转换为字符串
     */
    public static String toStr(Object value) {
        return toStr(value, null);
    }

    /**
     * 转换为整数
     */
    public static Integer toInt(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        final String valueStr = toStr(value, null);
        if (valueStr == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(valueStr.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为整数
     */
    public static Integer toInt(Object value) {
        return toInt(value, null);
    }
} 