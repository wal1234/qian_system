package com.qian.system.common.utils;

import com.github.pagehelper.PageHelper;
import com.qian.system.common.utils.sql.SqlUtil;

/**
 * 分页工具类
 */
public class PageUtils {
    /**
     * 设置请求分页数据
     */
    public static void startPage() {
        PageHelper.startPage(ServletUtils.getParameterToInt("pageNum"), ServletUtils.getParameterToInt("pageSize"));
    }

    /**
     * 设置请求排序数据
     */
    public static void startOrderBy() {
        String orderBy = SqlUtil.escapeOrderBySql(ServletUtils.getParameter("orderByColumn"));
        PageHelper.orderBy(orderBy);
    }
} 