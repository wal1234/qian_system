package com.qian.system.domain.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qian.common.annotation.Excel;
import com.qian.common.annotation.Excel.ColumnType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统访问记录表 sys_login_log
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_login_log")
@Schema(description = "系统访问记录")
public class SysLoginLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "info_id", type = IdType.AUTO)
    @Excel(name = "序号", cellType = ColumnType.NUMERIC)
    @Schema(description = "ID")
    private Long infoId;

    /** 用户账号 */
    @NotBlank(message = "用户账号不能为空")
    @Excel(name = "用户账号")
    @Schema(description = "用户账号")
    private String username;

    /** 登录IP地址 */
    @NotBlank(message = "登录IP地址不能为空")
    @Excel(name = "登录地址")
    @Schema(description = "登录IP地址")
    private String ipAddress;

    /** 登录地点 */
    @Excel(name = "登录地点")
    @Schema(description = "登录地点")
    private String loginLocation;

    /** 浏览器类型 */
    @Excel(name = "浏览器")
    @Schema(description = "浏览器类型")
    private String browser;

    /** 操作系统 */
    @Excel(name = "操作系统")
    @Schema(description = "操作系统")
    private String os;

    /** 登录状态（0成功 1失败） */
    @NotNull(message = "登录状态不能为空")
    @Pattern(regexp = "^[01]$", message = "登录状态只能是0或1")
    @Excel(name = "登录状态", readConverterExp = "0=成功,1=失败")
    @Schema(description = "登录状态（0成功 1失败）")
    private String status;

    /** 提示消息 */
    @Excel(name = "提示消息")
    @Schema(description = "提示消息")
    private String msg;

    /** 访问时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "访问时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "访问时间")
    private Date loginTime;
} 