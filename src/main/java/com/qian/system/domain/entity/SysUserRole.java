package com.qian.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户和角色关联 sys_user_role
 */
@Data
@TableName("sys_user_role")
@Schema(description = "用户和角色关联")
public class SysUserRole {
    /** 用户ID */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID")
    private Long userId;
    
    /** 角色ID */
    @NotNull(message = "角色ID不能为空")
    @Schema(description = "角色ID")
    private Long roleId;
} 