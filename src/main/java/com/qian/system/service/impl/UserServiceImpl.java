package com.qian.system.service.impl;

import com.qian.system.domain.User;
import com.qian.system.mapper.UserMapper;
import com.qian.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户业务层实现
 */
@Service
public class UserServiceImpl implements IUserService {
    
    @Autowired
    private UserMapper userMapper;

    /**
     * 查询用户列表
     *
     * @param user 用户信息
     * @return 用户集合
     */
    @Override
    public List<User> selectUserList(User user) {
        return userMapper.selectUserList(user);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param id 用户ID
     * @return 用户对象信息
     */
    @Override
    public User selectUserById(Long id) {
        return userMapper.selectUserById(id);
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(User user) {
        User info = userMapper.selectUserByUsername(user.getUsername());
        return info == null || info.getId().equals(user.getId());
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkPhoneUnique(User user) {
        User info = userMapper.selectUserByPhone(user.getPhone());
        return info == null || info.getId().equals(user.getId());
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkEmailUnique(User user) {
        User info = userMapper.selectUserByEmail(user.getEmail());
        return info == null || info.getId().equals(user.getId());
    }

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    /**
     * 批量删除用户信息
     *
     * @param ids 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(Long[] ids) {
        return userMapper.deleteUserByIds(ids);
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(User user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(User user) {
        return userMapper.updateUser(user);
    }

    /**
     * 判断是否存在管理员用户
     *
     * @param ids 用户ID数组
     * @return 结果
     */
    @Override
    public boolean hasAdminUser(Long[] ids) {
        return userMapper.countAdminUsers(ids) > 0;
    }
} 