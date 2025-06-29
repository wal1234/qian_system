package com.qian.system.mapper;

import java.util.List;
import com.qian.system.domain.entity.SysLoginLog;

/**
 * 系统访问日志情况信息 数据层
 */
public interface SysLoginLogMapper {
    /**
     * 新增系统登录日志
     * 
     * @param loginLog 访问日志对象
     */
    public void insertLoginLog(SysLoginLog loginLog);

    /**
     * 查询系统登录日志集合
     * 
     * @param loginLog 访问日志对象
     * @return 登录记录集合
     */
    public List<SysLoginLog> selectLoginLogList(SysLoginLog loginLog);

    /**
     * 批量删除系统登录日志
     * 
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    public int deleteLoginLogByIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     */
    public void cleanLoginLog();
} 