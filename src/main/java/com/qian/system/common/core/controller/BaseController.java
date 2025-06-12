package com.qian.system.common.core.controller;

import com.github.pagehelper.PageInfo;
import com.qian.system.common.core.page.TableDataInfo;
import com.qian.system.common.utils.PageUtils;
import com.qian.common.utils.DateUtils;
import com.qian.common.utils.SecurityUtils;
import com.qian.common.response.Response;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * Web层通用数据处理
 */
public class BaseController {
    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageUtils.startPage();
    }

    /**
     * 设置请求排序数据
     */
    protected void startOrderBy() {
        PageUtils.startOrderBy();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T> TableDataInfo<T> getDataTable(List<T> list) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 返回成功
     */
    public <T> Response<T> success() {
        return Response.success();
    }

    /**
     * 返回失败消息
     */
    public <T> Response<T> error() {
        return Response.error();
    }

    /**
     * 返回失败消息
     */
    public <T> Response<T> error(String message) {
        return Response.error(message);
    }

    /**
     * 返回成功消息
     */
    public Response<String> success(String message) {
        return Response.success(message);
    }

    /**
     * 获取用户ID
     */
    public Long getUserId() {
        return getUserId();
    }

    /**
     * 获取部门ID
     */
    public Long getDeptId() {
        return getDeptId();
    }

    /**
     * 获取用户账号
     */
    public String getUsername() {
        return SecurityUtils.getUsername();
    }
} 