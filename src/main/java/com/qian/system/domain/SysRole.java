package com.qian.system.domain;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色表 sys_role
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色信息")
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @Schema(description = "角色ID")
    private Long roleId;

    /** 角色名称 */
    @Schema(description = "角色名称", required = true)
    private String roleName;

    /** 角色权限 */
    @Schema(description = "角色权限", required = true)
    private String roleKey;

    /** 角色排序 */
    @Schema(description = "角色排序")
    private Integer roleSort;

    /** 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限） */
    @Schema(description = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限）")
    private String dataScope;

    /** 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示） */
    @Schema(description = "菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）")
    private boolean menuCheckStrictly;

    /** 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ） */
    @Schema(description = "部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）")
    private boolean deptCheckStrictly;

    /** 角色状态（0正常 1停用） */
    @Schema(description = "角色状态（0正常 1停用）")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    @Schema(description = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /** 用户是否存在此角色标识 默认不存在 */
    @Schema(description = "用户是否存在此角色标识 默认不存在")
    private boolean flag = false;

    /** 菜单组 */
    @Schema(description = "菜单组")
    private Long[] menuIds;

    /** 部门组（数据权限） */
    @Schema(description = "部门组（数据权限）")
    private Long[] deptIds;

    /** 角色菜单权限 */
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
} 