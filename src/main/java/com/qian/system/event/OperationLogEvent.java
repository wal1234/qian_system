package com.qian.system.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 操作日志事件
 */
@Getter
public class OperationLogEvent extends ApplicationEvent {
    private final String title;
    private final Integer businessType;
    private final String method;
    private final String requestMethod;
    private final Integer operatorType;
    private final String operationName;
    private final String operationIp;
    private final String operationLocation;
    private final String operationParam;
    private final String jsonResult;
    private final Integer status;
    private final String errorMsg;

    public OperationLogEvent(Object source, String title, Integer businessType, String method,
                           String requestMethod, Integer operatorType, String operationName,
                           String operationIp, String operationLocation, String operationParam,
                           String jsonResult, Integer status, String errorMsg) {
        super(source);
        this.title = title;
        this.businessType = businessType;
        this.method = method;
        this.requestMethod = requestMethod;
        this.operatorType = operatorType;
        this.operationName = operationName;
        this.operationIp = operationIp;
        this.operationLocation = operationLocation;
        this.operationParam = operationParam;
        this.jsonResult = jsonResult;
        this.status = status;
        this.errorMsg = errorMsg;
    }
} 