package com.qian.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统访问记录表 sys_login_log
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统访问记录")
public class SysLoginLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @Schema(description = "ID")
    private Long infoId;

    /** 用户账号 */
    @Schema(description = "用户账号")
    private String username;

    /** 登录IP地址 */
    @Schema(description = "登录IP地址")
    private String ipAddress;

    /** 登录地点 */
    @Schema(description = "登录地点")
    private String loginLocation;

    /** 浏览器类型 */
    @Schema(description = "浏览器类型")
    private String browser;

    /** 操作系统 */
    @Schema(description = "操作系统")
    private String os;

    /** 登录状态（0成功 1失败） */
    @Schema(description = "登录状态（0成功 1失败）")
    private String status;

    /** 提示消息 */
    @Schema(description = "提示消息")
    private String msg;
} 