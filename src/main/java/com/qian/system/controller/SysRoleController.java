package com.qian.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qian.common.annotation.Log;
import com.qian.system.common.core.controller.BaseController;
import com.qian.common.response.Response;
import com.qian.system.domain.SysRole;
import com.qian.common.enums.system.BusinessType;
import com.qian.system.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 角色信息
 */
@Slf4j
@Tag(name = "角色管理", description = "角色管理相关接口")
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService roleService;

    /**
     * 获取角色列表
     */
    @Operation(summary = "获取角色列表", description = "根据条件分页查询角色列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public Response<List<SysRole>> list(SysRole role) {
        List<SysRole> list = roleService.selectRoleList(role);
        return Response.success(list);
    }

    /**
     * 导出角色列表
     */
    @Operation(summary = "导出角色列表", description = "导出角色列表到Excel")
    @PreAuthorize("@ss.hasPermi('system:role:export')")
    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Response<Void> export(SysRole role) {
        List<SysRole> list = roleService.selectRoleList(role);
        // TODO: 实现导出逻辑
        return Response.success();
    }

    /**
     * 根据角色编号获取详细信息
     */
    @Operation(summary = "获取角色详细信息", description = "根据角色ID获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public Response<SysRole> getInfo(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        return Response.success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @Operation(summary = "新增角色", description = "新增角色信息")
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Response<Void> add(@Validated @RequestBody SysRole role) {
        if (!roleService.checkRoleNameUnique(role)) {
            return Response.error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return Response.error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(getUsername());
        return toResponse(roleService.insertRole(role));
    }

    /**
     * 修改保存角色
     */
    @Operation(summary = "修改角色", description = "修改角色信息")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Response<Void> edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        if (!roleService.checkRoleNameUnique(role)) {
            return Response.error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return Response.error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(getUsername());
        return toResponse(roleService.updateRole(role));
    }

    /**
     * 修改保存数据权限
     */
    @Operation(summary = "修改数据权限", description = "修改角色数据权限")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public Response<Void> dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(getUsername());
        return toResponse(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    @Operation(summary = "修改角色状态", description = "修改角色状态")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public Response<Void> changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(getUsername());
        return toResponse(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色", description = "删除角色信息")
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public Response<Void> remove(@Parameter(description = "角色ID串") @PathVariable Long[] roleIds) {
        return toResponse(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    @Operation(summary = "获取角色选择框列表", description = "获取角色选择框列表")
    @GetMapping("/optionselect")
    public Response<List<SysRole>> optionselect() {
        return Response.success(roleService.selectRoleAll());
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    private Response<Void> toResponse(int rows) {
        return rows > 0 ? Response.success() : Response.error("操作失败");
    }
} 