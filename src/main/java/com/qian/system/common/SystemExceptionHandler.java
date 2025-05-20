package com.qian.system.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionOverrideException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.qian.common.response.Response;

/**
 * 系统模块异常处理器
 */
@RestControllerAdvice("com.qian.system")
@ConditionalOnMissingBean(name = "globalExceptionHandler")
public class SystemExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(SystemExceptionHandler.class);

    /**
     * 处理Spring配置异常
     */
    @ExceptionHandler(BeanDefinitionStoreException.class)
    public Response<Void> handleBeanDefinitionStoreException(BeanDefinitionStoreException e) {
        log.error("Spring配置异常", e);
        if (e.getCause() instanceof BeanDefinitionOverrideException) {
            return Response.error("存在重复的Bean定义，请检查组件扫描配置");
        }
        return Response.error("Spring配置异常：" + e.getMessage());
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Response<Void> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return Response.error(e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Response.error(message);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Response<Void> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Response.error(message);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public Response<Void> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Response.error("系统错误，请联系管理员");
    }
} 