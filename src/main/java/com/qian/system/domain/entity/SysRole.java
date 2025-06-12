package com.qian.system.domain.entity;

import java.util.Set;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qian.common.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 角色表 sys_role
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
@Schema(description = "角色信息")
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @TableId(value = "role_id", type = IdType.AUTO)
    @Schema(description = "角色ID")
    private Long roleId;

    /** 角色名称 */
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 2, max = 20, message = "角色名称长度必须在2到20个字符之间")
    @Schema(description = "角色名称", required = true)
    private String roleName;

    /** 角色权限 */
    @NotBlank(message = "角色权限不能为空")
    @Size(min = 2, max = 100, message = "角色权限长度必须在2到100个字符之间")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "角色权限必须以字母开头，只能包含字母、数字和下划线")
    @Schema(description = "角色权限", required = true)
    private String roleKey;

    /** 角色排序 */
    @NotNull(message = "角色排序不能为空")
    @Schema(description = "角色排序")
    private Integer roleSort;

    /** 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限） */
    @NotBlank(message = "数据范围不能为空")
    @Pattern(regexp = "^[1-5]$", message = "数据范围只能是1到5之间的数字")
    @Schema(description = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限）")
    private String dataScope;

    /** 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示） */
    @Schema(description = "菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）")
    private boolean menuCheckStrictly;

    /** 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ） */
    @Schema(description = "部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）")
    private boolean deptCheckStrictly;

    /** 角色状态（0正常 1停用） */
    @NotBlank(message = "角色状态不能为空")
    @Pattern(regexp = "^[01]$", message = "角色状态只能是0或1")
    @Schema(description = "角色状态（0正常 1停用）")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    @Pattern(regexp = "^[02]$", message = "删除标志只能是0或2")
    @Schema(description = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /** 用户是否存在此角色标识 默认不存在 */
    @TableField(exist = false)
    @Schema(description = "用户是否存在此角色标识 默认不存在")
    private boolean flag = false;

    /** 菜单组 */
    @TableField(exist = false)
    @Schema(description = "菜单组")
    private Long[] menuIds;

    /** 部门组（数据权限） */
    @TableField(exist = false)
    @Schema(description = "部门组（数据权限）")
    private Long[] deptIds;

    /** 角色菜单权限 */
    @TableField(exist = false)
    @Schema(description = "角色菜单权限")
    private Set<String> permissions;

    /** 数据权限范围枚举 */
    public enum DataScope {
        ALL("1", "全部数据权限"),
        CUSTOM("2", "自定数据权限"),
        DEPT("3", "本部门数据权限"),
        DEPT_AND_CHILD("4", "本部门及以下数据权限"),
        SELF("5", "仅本人数据权限");

        private final String code;
        private final String info;

        DataScope(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
        
        public static DataScope getByCode(String code) {
            for (DataScope scope : values()) {
                if (scope.getCode().equals(code)) {
                    return scope;
                }
            }
            return null;
        }
    }

    public SysRole() {
    }

    public SysRole(Long roleId) {
        this.roleId = roleId;
    }

    public boolean isAdmin() {
        return isAdmin(this.roleKey);
    }

    public static boolean isAdmin(String roleKey) {
        return roleKey != null && "admin".equals(roleKey);
    }

    /**
     * 获取数据权限SQL
     */
    public String getDataScopeSql() {
        DataScope scope = DataScope.getByCode(this.dataScope);
        if (scope == null) {
            return "";
        }
        
        StringBuilder sql = new StringBuilder();
        switch (scope) {
            case ALL:
                sql.append("1=1");
                break;
            case CUSTOM:
                sql.append("dept_id in (select dept_id from sys_role_dept where role_id = ").append(this.roleId).append(")");
                break;
            case DEPT:
                sql.append("dept_id = ").append(SecurityUtils.getDeptId());
                break;
            case DEPT_AND_CHILD:
                sql.append("dept_id in (select dept_id from sys_dept where dept_id = ").append(SecurityUtils.getDeptId())
                   .append(" or find_in_set(").append(SecurityUtils.getDeptId()).append(", ancestors))");
                break;
            case SELF:
                sql.append("create_by = '").append(SecurityUtils.getUsername()).append("'");
                break;
            default:
                sql.append("1=1");
        }
        return sql.toString();
    }
} 