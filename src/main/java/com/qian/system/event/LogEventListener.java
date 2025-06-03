package com.qian.system.event;

import com.qian.system.domain.SysLoginLog;
import com.qian.system.domain.SysOperLog;
import com.qian.system.service.ISysLoginLogService;
import com.qian.system.service.ISysOperLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 日志事件监听器
 */
@Slf4j
@Component
public class LogEventListener {

    @Autowired
    private ISysOperLogService operLogService;

    @Autowired
    private ISysLoginLogService loginLogService;

    /**
     * 异步处理操作日志事件
     */
    @Async
    @EventListener
    public void handleOperationLogEvent(OperationLogEvent event) {
        try {
            SysOperLog operLog = new SysOperLog();
            operLog.setTitle(event.getTitle());
            operLog.setBusinessType(event.getBusinessType());
            operLog.setMethod(event.getMethod());
            operLog.setRequestMethod(event.getRequestMethod());
            operLog.setOperatorType(event.getOperatorType());
            operLog.setOperationName(event.getOperationName());
            operLog.setOperationIp(event.getOperationIp());
            operLog.setOperationLocation(event.getOperationLocation());
            operLog.setOperationParam(event.getOperationParam());
            operLog.setJsonResult(event.getJsonResult());
            operLog.setStatus(event.getStatus());
            operLog.setErrorMsg(event.getErrorMsg());
            
            operLogService.insertOperLog(operLog);
        } catch (Exception e) {
            log.error("处理操作日志事件时发生错误", e);
        }
    }

    /**
     * 异步处理登录日志事件
     */
    @Async
    @EventListener
    public void handleLoginLogEvent(LoginLogEvent event) {
        try {
            SysLoginLog loginLog = new SysLoginLog();
            loginLog.setUsername(event.getUsername());
            loginLog.setIpAddress(event.getIpAddress());
            loginLog.setLoginLocation(event.getLoginLocation());
            loginLog.setBrowser(event.getBrowser());
            loginLog.setOs(event.getOs());
            loginLog.setStatus(event.getStatus());
            loginLog.setMsg(event.getMsg());
            
            loginLogService.insertLoginLog(loginLog);
        } catch (Exception e) {
            log.error("处理登录日志事件时发生错误", e);
        }
    }
} 