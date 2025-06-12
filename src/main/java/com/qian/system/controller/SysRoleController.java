package com.qian.system.controller;

import java.util.List;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qian.common.annotation.Log;
import com.qian.system.common.core.controller.BaseController;
import com.qian.common.response.Response;
import com.qian.system.domain.entity.SysRole;
import com.qian.common.enums.system.BusinessType;
import com.qian.system.service.ISysRoleService;
import com.qian.system.service.ISysOperLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.qian.common.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.qian.common.utils.SecurityUtils;
import com.qian.system.domain.entity.SysUser;
import com.qian.system.service.ISysUserService;
import com.qian.system.domain.entity.SysUserRole;

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

    @Autowired
    private ISysOperLogService operLogService;

    @Autowired
    private ISysUserService userService;

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
    @Log(title = "角色管理", businessType = "EXPORT")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRole role) throws IOException {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<>(SysRole.class);
        util.exportExcel(response, list, "角色数据");
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
    @Log(title = "角色管理", businessType = "INSERT")
    @PostMapping
    public Response<Void> add(@Validated @RequestBody SysRole role) {
        if (!roleService.checkRoleNameUnique(role)) {
            return Response.error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        role.setCreateBy(SecurityUtils.getUsername());
        return Response.success();
    }

    /**
     * 修改保存角色
     */
    @Operation(summary = "修改角色", description = "修改角色信息")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = "UPDATE")
    @PutMapping
    public Response<Void> edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        if (!roleService.checkRoleNameUnique(role)) {
            return Response.error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        role.setUpdateBy(SecurityUtils.getUsername());
        return Response.success();
    }

    /**
     * 修改保存数据权限
     */
    @Operation(summary = "修改数据权限", description = "修改角色数据权限")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = "UPDATE")
    @PutMapping("/dataScope")
    public Response<Void> dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(SecurityUtils.getUsername());
        int rows = roleService.authDataScope(role);
        log.info("修改角色数据权限: {}", role);
        return toResponse(rows);
    }

    /**
     * 状态修改
     */
    @Operation(summary = "修改角色状态", description = "修改角色状态")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = "UPDATE")
    @PutMapping("/changeStatus")
    public Response<Void> changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(SecurityUtils.getUsername());
        return Response.success();
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色", description = "删除角色信息")
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "角色管理", businessType = "DELETE")
    @DeleteMapping("/{roleIds}")
    public Response<Void> remove(@Parameter(description = "角色ID串") @PathVariable Long[] roleIds) {
        int rows = roleService.deleteRoleByIds(roleIds);
        log.info("删除角色: {}", Arrays.toString(roleIds));
        return toResponse(rows);
    }

    /**
     * 获取角色选择框列表
     */
    @Operation(summary = "获取角色选择框列表", description = "获取角色选择框列表")
    @GetMapping("/optionselect")
    public Response<List<SysRole>> optionselect() {
        List<SysRole> roles = roleService.selectRoleAll();
        return Response.success(roles);
    }

    /**
     * 导入角色数据
     */
    @Operation(summary = "导入角色数据", description = "导入角色数据")
    @PreAuthorize("@ss.hasPermi('system:role:import')")
    @Log(title = "角色管理", businessType = "IMPORT")
    @PostMapping("/importData")
    public Response<String> importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysRole> util = new ExcelUtil<>(SysRole.class);
        List<SysRole> roleList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = roleService.importRole(roleList, updateSupport, operName);
        return Response.success(message);
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

    @GetMapping("/authUser/allocatedList")
    public Response<List<SysUser>> allocatedList(SysUser user) {
        List<SysUser> list = userService.selectAllocatedList(user);
        return Response.success(list);
    }

    @GetMapping("/authUser/unallocatedList")
    public Response<List<SysUser>> unallocatedList(SysUser user) {
        List<SysUser> list = userService.selectUnallocatedList(user);
        return Response.success(list);
    }

    @PostMapping("/authUser/selectAll")
    public Response<String> selectAuthUserAll(Long roleId, Long[] userIds) {
        return Response.success();
    }

    @PostMapping("/authUser/cancel")
    public Response<String> cancelAuthUser(@RequestBody SysUserRole userRole) {
        return Response.success();
    }

    @PostMapping("/authUser/cancelAll")
    public Response<String> cancelAuthUserAll(Long roleId, Long[] userIds) {
        return Response.success();
    }
} 