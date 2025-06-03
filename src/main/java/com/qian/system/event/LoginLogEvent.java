package com.qian.system.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 登录日志事件
 */
@Getter
public class LoginLogEvent extends ApplicationEvent {
    private final String username;
    private final String ipAddress;
    private final String loginLocation;
    private final String browser;
    private final String os;
    private final String status;
    private final String msg;

    public LoginLogEvent(Object source, String username, String ipAddress, String loginLocation,
                        String browser, String os, String status, String msg) {
        super(source);
        this.username = username;
        this.ipAddress = ipAddress;
        this.loginLocation = loginLocation;
        this.browser = browser;
        this.os = os;
        this.status = status;
        this.msg = msg;
    }
} 