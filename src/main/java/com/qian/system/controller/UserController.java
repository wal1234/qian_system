package com.qian.system.controller;

import com.qian.system.common.Result;
import com.qian.system.domain.User;
import com.qian.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息
 */
@RestController
@RequestMapping("/system/user")
public class UserController {
    
    @Autowired
    private IUserService userService;
    
    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public Result<List<User>> list(User user) {
        List<User> list = userService.selectUserList(user);
        return Result.success(list);
    }
    
    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping("/{id}")
    public Result<User> getInfo(@PathVariable Long id) {
        return Result.success(userService.selectUserById(id));
    }
    
    /**
     * 新增用户
     */
    @PostMapping
    public Result<Void> add(@RequestBody User user) {
        if (!userService.checkUserNameUnique(user)) {
            return Result.error("新增用户'" + user.getUsername() + "'失败，登录账号已存在");
        } else if (!userService.checkPhoneUnique(user)) {
            return Result.error("新增用户'" + user.getUsername() + "'失败，手机号码已存在");
        } else if (!userService.checkEmailUnique(user)) {
            return Result.error("新增用户'" + user.getUsername() + "'失败，邮箱账号已存在");
        }
        return toResult(userService.insertUser(user));
    }
    
    /**
     * 修改用户
     */
    @PutMapping
    public Result<Void> edit(@RequestBody User user) {
        if (!userService.checkUserNameUnique(user)) {
            return Result.error("修改用户'" + user.getUsername() + "'失败，登录账号已存在");
        } else if (!userService.checkPhoneUnique(user)) {
            return Result.error("修改用户'" + user.getUsername() + "'失败，手机号码已存在");
        } else if (!userService.checkEmailUnique(user)) {
            return Result.error("修改用户'" + user.getUsername() + "'失败，邮箱账号已存在");
        }
        return toResult(userService.updateUser(user));
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@PathVariable Long[] ids) {
        if (userService.hasAdminUser(ids)) {
            return Result.error("不允许删除超级管理员用户");
        }
        return toResult(userService.deleteUserByIds(ids));
    }
    
    /**
     * 重置密码
     */
    @PutMapping("/resetPwd")
    public Result<Void> resetPwd(@RequestBody User user) {
        return toResult(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PutMapping("/changeStatus")
    public Result<Void> changeStatus(@RequestBody User user) {
        return toResult(userService.updateUserStatus(user));
    }

    /**
     * 更新用户头像
     */
    @PutMapping("/updateAvatar")
    public Result<Void> updateAvatar(@RequestBody User user) {
        return toResult(userService.updateUser(user));
    }

    /**
     * 更新用户个人信息
     */
    @PutMapping("/updateProfile")
    public Result<Void> updateProfile(@RequestBody User user) {
        return toResult(userService.updateUser(user));
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    private Result<Void> toResult(int rows) {
        return rows > 0 ? Result.success() : Result.error("操作失败");
    }
} 