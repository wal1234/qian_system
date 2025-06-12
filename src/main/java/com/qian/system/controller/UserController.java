package com.qian.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.qian.common.response.Response;
import com.qian.system.domain.entity.User;
import com.qian.system.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.qian.common.utils.SecurityUtils;

/**
 * 用户信息
 */
@Slf4j
@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/system/user2")
public class UserController {
    @Autowired
    private IUserService userService;

    @Operation(summary = "获取用户列表")
    @GetMapping("/list")
    public Response<List<User>> list(User user) {
        List<User> list = userService.selectUserList(user);
        return Response.success(list);
    }

    @Operation(summary = "根据用户编号获取详细信息")
    @GetMapping("/{id}")
    public Response<User> getInfo(@Parameter(description = "用户ID") @PathVariable Long id) {
        return Response.success(userService.selectUserById(id));
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public Response<Void> add(@RequestBody User user) {
        if (!userService.checkUserNameUnique(user)) {
            return Response.error("新增用户'" + user.getUsername() + "'失败，登录账号已存在");
        } else if (!userService.checkPhoneUnique(user)) {
            return Response.error("新增用户'" + user.getUsername() + "'失败，手机号码已存在");
        } else if (!userService.checkEmailUnique(user)) {
            return Response.error("新增用户'" + user.getUsername() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        return toResponse(userService.insertUser(user));
    }

    @Operation(summary = "修改用户")
    @PutMapping
    public Response<Void> edit(@RequestBody User user) {
        if (!userService.checkUserNameUnique(user)) {
            return Response.error("修改用户'" + user.getUsername() + "'失败，登录账号已存在");
        } else if (!userService.checkPhoneUnique(user)) {
            return Response.error("修改用户'" + user.getUsername() + "'失败，手机号码已存在");
        } else if (!userService.checkEmailUnique(user)) {
            return Response.error("修改用户'" + user.getUsername() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(userService.updateUser(user));
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{ids}")
    public Response<Void> remove(@Parameter(description = "用户ID串") @PathVariable Long[] ids) {
        if (userService.hasAdminUser(ids)) {
            return Response.error("不允许删除超级管理员用户");
        }
        return toResponse(userService.deleteUserByIds(ids));
    }

    @Operation(summary = "重置密码")
    @PutMapping("/resetPwd")
    public Response<Void> resetPwd(@RequestBody User user) {
        return toResponse(userService.resetPwd(user));
    }

    @Operation(summary = "状态修改")
    @PutMapping("/changeStatus")
    public Response<Void> changeStatus(@RequestBody User user) {
        return toResponse(userService.updateUserStatus(user));
    }

    @Operation(summary = "更新用户头像")
    @PutMapping("/updateAvatar")
    public Response<Void> updateAvatar(@RequestBody User user) {
        return toResponse(userService.updateUser(user));
    }

    @Operation(summary = "更新用户个人信息")
    @PutMapping("/updateProfile")
    public Response<Void> updateProfile(@RequestBody User user) {
        return toResponse(userService.updateUser(user));
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