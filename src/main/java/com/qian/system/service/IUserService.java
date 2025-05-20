package com.qian.system.service;

import com.qian.system.domain.User;
import java.util.List;

/**
 * 用户 服务层
 */
public interface IUserService {
    /**
     * 查询用户列表
     * 
     * @param user 用户信息
     * @return 用户列表
     */
    List<User> selectUserList(User user);

    /**
     * 根据用户ID查询用户
     * 
     * @param id 用户ID
     * @return 用户对象信息
     */
    User selectUserById(Long id);

    /**
     * 校验用户名称是否唯一
     * 
     * @param user 用户信息
     * @return 结果
     */
    boolean checkUserNameUnique(User user);

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean checkPhoneUnique(User user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean checkEmailUnique(User user);

    /**
     * 新增用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    int insertUser(User user);

    /**
     * 修改用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    int updateUser(User user);

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的用户ID
     * @return 结果
     */
    int deleteUserByIds(Long[] ids);

    /**
     * 重置用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    int resetPwd(User user);

    /**
     * 修改用户状态
     * 
     * @param user 用户信息
     * @return 结果
     */
    int updateUserStatus(User user);

    /**
     * 判断是否存在管理员用户
     * 
     * @param ids 用户ID数组
     * @return 结果
     */
    boolean hasAdminUser(Long[] ids);
} 