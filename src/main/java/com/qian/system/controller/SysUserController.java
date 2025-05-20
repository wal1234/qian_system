package com.qian.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qian.common.annotation.Log;
import com.qian.system.common.core.controller.BaseController;
import com.qian.common.response.Response;
import com.qian.system.domain.entity.SysUser;
import com.qian.common.enums.system.BusinessType;
import com.qian.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 用户信息
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Operation(summary = "获取用户列表")
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public Response<List<SysUser>> list(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        return Response.success(list);
    }

    @Operation(summary = "导出用户列表")
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Response<Void> export(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        // TODO: 实现导出逻辑
        return Response.success();
    }

    @Operation(summary = "根据用户编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = "/{userId}")
    public Response<SysUser> getInfo(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return Response.success(userService.selectUserById(userId));
    }

    @Operation(summary = "新增用户")
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Response<Void> add(@Validated @RequestBody SysUser user) {
        if (!userService.checkUserNameUnique(user)) {
            return Response.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (!userService.checkPhoneUnique(user)) {
            return Response.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (!userService.checkEmailUnique(user)) {
            return Response.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(getUsername());
        return toResponse(userService.insertUser(user));
    }

    @Operation(summary = "修改用户")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Response<Void> edit(@Validated @RequestBody SysUser user) {
        if (!userService.checkUserNameUnique(user)) {
            return Response.error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (!userService.checkPhoneUnique(user)) {
            return Response.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (!userService.checkEmailUnique(user)) {
            return Response.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getUsername());
        return toResponse(userService.updateUser(user));
    }

    @Operation(summary = "删除用户")
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public Response<Void> remove(@Parameter(description = "用户ID串") @PathVariable Long[] userIds) {
        if (userService.hasAdminUser(userIds)) {
            return Response.error("不允许删除超级管理员用户");
        }
        return toResponse(userService.deleteUserByIds(userIds));
    }

    @Operation(summary = "重置密码")
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public Response<Void> resetPwd(@RequestBody SysUser user) {
        return toResponse(userService.resetPwd(user));
    }

    @Operation(summary = "状态修改")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public Response<Void> changeStatus(@RequestBody SysUser user) {
        return toResponse(userService.updateUserStatus(user));
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