package com.qian.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_menu")
@Schema(description = "菜单权限信息")
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 菜单ID */
    @TableId(type = IdType.AUTO)
    @Schema(description = "菜单ID")
    private Long menuId;

    /** 菜单名称 */
    @Schema(description = "菜单名称")
    private String menuName;

    /** 父菜单ID */
    @Schema(description = "父菜单ID")
    private Long parentId;

    /** 显示顺序 */
    @Schema(description = "显示顺序")
    private Integer orderNum;

    /** 路由地址 */
    @Schema(description = "路由地址")
    private String path;

    /** 组件路径 */
    @Schema(description = "组件路径")
    private String component;

    /** 路由参数 */
    @Schema(description = "路由参数")
    private String query;

    /** 是否为外链（0是 1否） */
    @Schema(description = "是否为外链（0是 1否）")
    private Integer isFrame;

    /** 是否缓存（0缓存 1不缓存） */
    @Schema(description = "是否缓存（0缓存 1不缓存）")
    private Integer isCache;

    /** 菜单类型（M目录 C菜单 F按钮） */
    @Schema(description = "菜单类型（M目录 C菜单 F按钮）")
    private String menuType;

    /** 菜单状态（0显示 1隐藏） */
    @Schema(description = "菜单状态（0显示 1隐藏）")
    private Integer visible;

    /** 菜单状态（0正常 1停用） */
    @Schema(description = "菜单状态（0正常 1停用）")
    private Integer status;

    /** 权限标识 */
    @Schema(description = "权限标识")
    private String perms;

    /** 菜单图标 */
    @Schema(description = "菜单图标")
    private String icon;

    /** 创建者 */
    @Schema(description = "创建者")
    private String createBy;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /** 更新者 */
    @Schema(description = "更新者")
    private String updateBy;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 子菜单 */
    @TableField(exist = false)
    @Schema(description = "子菜单")
    private List<SysMenu> children = new ArrayList<>();
} 