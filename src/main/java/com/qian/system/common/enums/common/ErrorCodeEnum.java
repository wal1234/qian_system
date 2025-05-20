package com.qian.system.common.enums.common;

public enum ErrorCodeEnum {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    
    // 用户相关错误码
    USER_NOT_EXIST(1001, "用户不存在"),
    USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误"),
    USER_ACCOUNT_EXPIRED(1003, "账号已过期"),
    USER_CREDENTIALS_ERROR(1004, "密码错误"),
    USER_CREDENTIALS_EXPIRED(1005, "密码过期"),
    USER_ACCOUNT_DISABLE(1006, "账号不可用"),
    USER_ACCOUNT_LOCKED(1007, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(1008, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(1009, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(1010, "账号下线"),
    
    // 业务相关错误码
    BUSINESS_ERROR(2001, "业务异常"),
    SYSTEM_ERROR(2002, "系统异常"),
    PARAM_ERROR(2003, "参数错误"),
    DATA_NOT_EXIST(2004, "数据不存在"),
    DATA_ALREADY_EXIST(2005, "数据已存在"),
    
    // 权限相关错误码
    NO_PERMISSION(3001, "无权限访问"),
    TOKEN_EXPIRED(3002, "token已过期"),
    TOKEN_INVALID(3003, "token无效");

    private final int code;
    private final String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 