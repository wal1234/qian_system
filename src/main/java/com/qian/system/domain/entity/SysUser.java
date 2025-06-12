package com.qian.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 用户对象 sys_user
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
@Schema(description = "用户对象")
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "用户ID")
    private Long id;

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2到20个字符之间")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "用户名必须以字母开头，只能包含字母、数字和下划线")
    @Schema(description = "用户名")
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6到20个字符之间")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "密码")
    private String password;

    /** 昵称 */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    @Schema(description = "昵称")
    private String nickname;

    /** 邮箱 */
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    @Schema(description = "邮箱")
    private String email;

    /** 头像地址 */
    @Size(max = 100, message = "头像地址长度不能超过100个字符")
    @Schema(description = "头像地址")
    private String avatar;

    /** 手机号码 */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    @Schema(description = "手机号码")
    private String phone;

    /** 角色 */
    @NotBlank(message = "角色不能为空")
    @Schema(description = "角色")
    private String role;

    /** 状态（normal:正常 disabled:禁用） */
    @NotBlank(message = "状态不能为空")
    @Pattern(regexp = "^(normal|disabled)$", message = "状态只能是normal或disabled")
    @Schema(description = "状态（normal:正常 disabled:禁用）")
    private String status;

    /** 最后登录IP */
    @Size(max = 50, message = "IP地址长度不能超过50个字符")
    @Schema(description = "最后登录IP")
    private String loginIp;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后登录时间")
    private LocalDateTime loginDate;

    /** 删除标志（0代表存在 1代表删除） */
    @TableField("del_flag")
    @Schema(description = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

    /** 最后密码重置时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后密码重置时间")
    private LocalDateTime lastPasswordResetTime;

    /** 密码重置令牌 */
    @JsonIgnore
    @Schema(description = "密码重置令牌")
    private String passwordResetToken;

    /** 令牌过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "令牌过期时间")
    private LocalDateTime tokenExpiryTime;
} 