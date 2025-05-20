package com.qian.system.common.response;

import com.qian.system.common.enums.common.ErrorCodeEnum;

public class Response<T> {
    private int code;
    private String message;
    private T data;

    private Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(200, "操作成功", data);
    }

    /**
     * 成功，无数据
     */
    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> error(int code, String message) {
        return new Response<>(code, message, null);
    }

    public static <T> Response<T> error(ErrorCodeEnum errorCode) {
        return new Response<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    // Getter methods
    public int getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }

    // Setter methods
    public void setCode(int code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    public void setData(T data) { this.data = data; }
} 