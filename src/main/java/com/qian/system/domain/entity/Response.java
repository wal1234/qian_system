package com.qian.system.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 统一响应格式
 */
@Data
@Schema(description = "统一响应格式")
public class Response<T> {
    /** 状态码 */
    @NotNull(message = "状态码不能为空")
    @Schema(description = "状态码")
    private int code;

    /** 消息 */
    @Schema(description = "消息")
    private String message;

    /** 数据 */
    @Schema(description = "数据")
    private T data;

    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(200, "操作成功", data);
    }

    public static <T> Response<T> success() {
        return new Response<>(200, "操作成功", null);
    }

    public static <T> Response<T> error(int code, String message) {
        return new Response<>(code, message, null);
    }
} 