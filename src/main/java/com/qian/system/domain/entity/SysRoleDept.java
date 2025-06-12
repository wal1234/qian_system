package com.qian.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 角色和部门关联 sys_role_dept
 */
@Data
@TableName("sys_role_dept")
@Schema(description = "角色和部门关联")
public class SysRoleDept {
    /** 角色ID */
    @NotNull(message = "角色ID不能为空")
    @Schema(description = "角色ID")
    private Long roleId;
    
    /** 部门ID */
    @NotNull(message = "部门ID不能为空")
    @Schema(description = "部门ID")
    private Long deptId;
} 