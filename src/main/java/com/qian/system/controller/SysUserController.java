package com.qian.system.controller;

import java.util.List;
import java.io.IOException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import com.qian.common.utils.FileUploadUtils;
import com.qian.common.utils.SecurityUtils;

/**
 * 用户信息
 */
@Slf4j
@Tag(name = "用户管理", description = "用户管理相关接口")
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
    @Log(title = "用户管理", businessType = "EXPORT")
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysUser user) throws IOException {
        List<SysUser> list = userService.selectUserList(user);
        // TODO: 实现导出逻辑
    }

    @Operation(summary = "根据用户编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = "/{id}")
    public Response<SysUser> getInfo(@Parameter(description = "用户ID") @PathVariable Long id) {
        return Response.success(userService.selectUserById(id));
    }

    @Operation(summary = "新增用户")
    @Log(title = "用户管理", businessType = "INSERT")
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @PostMapping
    public Response<Void> add(@Validated @RequestBody SysUser user) {
        if (!userService.checkUserNameUnique(user.getUsername())) {
            return Response.error("新增用户'" + user.getUsername() + "'失败，登录账号已存在");
        } else if (!userService.checkPhoneUnique(user)) {
            return Response.error("新增用户'" + user.getUsername() + "'失败，手机号码已存在");
        } else if (!userService.checkEmailUnique(user)) {
            return Response.error("新增用户'" + user.getUsername() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(getUsername());
        return toAjax(userService.insertUser(user));
    }

    @Operation(summary = "修改用户")
    @Log(title = "用户管理", businessType = "UPDATE")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PutMapping
    public Response<Void> edit(@Validated @RequestBody SysUser user) {
        if (!userService.checkUserNameUnique(user.getUsername())) {
            return Response.error("修改用户'" + user.getUsername() + "'失败，登录账号已存在");
        } else if (!userService.checkPhoneUnique(user)) {
            return Response.error("修改用户'" + user.getUsername() + "'失败，手机号码已存在");
        } else if (!userService.checkEmailUnique(user)) {
            return Response.error("修改用户'" + user.getUsername() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }

    @Operation(summary = "删除用户")
    @Log(title = "用户管理", businessType = "DELETE")
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @DeleteMapping("/{ids}")
    public Response<Void> remove(@Parameter(description = "用户ID串") @PathVariable Long[] ids) {
        if (userService.hasAdminUser(ids)) {
            return Response.error("不允许删除超级管理员用户");
        }
        return toAjax(userService.deleteUserByIds(ids));
    }

    @Operation(summary = "重置密码")
    @Log(title = "用户管理", businessType = "UPDATE")
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @PutMapping("/resetPwd")
    public Response<Void> resetPwd(@RequestBody SysUser user) {
        return toAjax(userService.resetPwd(user));
    }

    @Operation(summary = "状态修改")
    @Log(title = "用户管理", businessType = "UPDATE")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PutMapping("/changeStatus")
    public Response<Void> changeStatus(@RequestBody SysUser user) {
        return toAjax(userService.updateUserStatus(user));
    }

    @Operation(summary = "更新用户头像")
    @Log(title = "用户管理", businessType = "UPDATE")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PostMapping("/updateAvatar")
    public Response<Void> updateAvatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Response.error("上传头像图片不能为空");
        }
        String avatar = FileUploadUtils.upload(file);
        boolean success = userService.updateUserAvatar(SecurityUtils.getUsername(), avatar);
        return success ? Response.success() : Response.error("更新头像失败");
    }

    @Operation(summary = "更新用户个人信息")
    @Log(title = "个人信息", businessType = "UPDATE")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PutMapping("/updateProfile")
    public Response<Void> updateProfile(@RequestBody SysUser user) {
        return toAjax(userService.updateUserProfile(user));
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    private Response<Void> toAjax(int rows) {
        return rows > 0 ? Response.success() : Response.error("操作失败");
    }
} 