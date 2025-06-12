package com.qian.system.domain.entity;

import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户对象 sys_user
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
@Schema(description = "用户对象")
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "用户ID")
    private Long id;

    /** 用户账号 */
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 2, max = 20, message = "用户账号长度必须在2到20个字符之间")
    @Schema(description = "用户账号")
    private String username;

    /** 用户昵称 */
    @NotBlank(message = "用户昵称不能为空")
    @Size(min = 2, max = 20, message = "用户昵称长度必须在2到20个字符之间")
    @Schema(description = "用户昵称")
    private String nickname;

    /** 用户邮箱 */
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    @Schema(description = "用户邮箱")
    private String email;

    /** 手机号码 */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    @Schema(description = "手机号码")
    private String phone;

    /** 用户性别 */
    @Pattern(regexp = "^[01]$", message = "用户性别只能是0或1")
    @Schema(description = "用户性别")
    private String sex;

    /** 用户头像 */
    @Schema(description = "用户头像")
    private String avatar;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6到20个字符之间")
    @Schema(description = "密码")
    private String password;

    /** 帐号状态（0正常 1停用） */
    @NotNull(message = "帐号状态不能为空")
    @Pattern(regexp = "^[01]$", message = "帐号状态只能是0或1")
    @Schema(description = "帐号状态（0正常 1停用）")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Pattern(regexp = "^[01]$", message = "删除标志只能是0或1")
    @Schema(description = "删除标志（0代表存在 1代表删除）")
    private String deleted;

    /** 最后登录IP */
    @Schema(description = "最后登录IP")
    private String loginIp;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后登录时间")
    private Date loginDate;

    /** 角色对象 */
    @TableField(exist = false)
    @Schema(description = "角色对象")
    private List<SysRole> roles;

    /** 角色组 */
    @TableField(exist = false)
    @Schema(description = "角色组")
    private Long[] roleIds;

    /** 岗位组 */
    @TableField(exist = false)
    @Schema(description = "岗位组")
    private Long[] postIds;

    /** 角色ID */
    @TableField(exist = false)
    @Schema(description = "角色ID")
    private Long roleId;
} 