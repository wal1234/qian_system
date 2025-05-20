package com.qian.system.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final String YYYY = "yyyy";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期转字符串
     */
    public static String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 字符串转日期
     */
    public static Date parseStrToDate(final String format, final String ts) throws ParseException {
        return new SimpleDateFormat(format).parse(ts);
    }

    /**
     * 获取当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期字符串
     */
    public static String getDate() {
        return parseDateToStr(YYYY_MM_DD, getNowDate());
    }

    /**
     * 获取当前时间字符串
     */
    public static String getTime() {
        return parseDateToStr(YYYY_MM_DD_HH_MM_SS, getNowDate());
    }
} 