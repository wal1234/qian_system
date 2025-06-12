package com.qian.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 角色和菜单关联 sys_role_menu
 */
@Data
@TableName("sys_role_menu")
@Schema(description = "角色和菜单关联")
public class SysRoleMenu {
    /** 角色ID */
    @NotNull(message = "角色ID不能为空")
    @Schema(description = "角色ID")
    private Long roleId;
    
    /** 菜单ID */
    @NotNull(message = "菜单ID不能为空")
    @Schema(description = "菜单ID")
    private Long menuId;
} 