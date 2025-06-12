package com.qian.system.exception;

import com.qian.common.exception.BaseException;

/**
 * 参数配置异常
 */
public class ConfigException extends BaseException {
    private static final long serialVersionUID = 1L;

    public ConfigException() {
        super("参数配置", null, null);
    }

    public ConfigException(String message) {
        super("参数配置", null, message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
} 