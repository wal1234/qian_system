package com.qian.system.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qian.common.constants.UserConstants;
import com.qian.system.domain.SysUser;
import com.qian.system.domain.SysRole;
import com.qian.system.domain.SysPost;
import com.qian.system.domain.SysUserRole;
import com.qian.system.common.exception.ServiceException;
import com.qian.system.common.utils.StringUtils;
import com.qian.system.common.utils.SecurityUtils;
import com.qian.system.mapper.SysUserMapper;
import com.qian.system.mapper.SysRoleMapper;
import com.qian.system.mapper.SysPostMapper;
import com.qian.system.mapper.SysUserRoleMapper;
import com.qian.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户 业务层处理
 */
@Slf4j
@Service
public class SysUserServiceImpl implements ISysUserService {
    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    /**
     * 判断是否是管理员
     */
    private boolean isAdmin() {
        return SecurityUtils.isAdmin(SecurityUtils.getLoginUser().getUserId());
    }

    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    /**
     * 通过用户名查询用户
     * 
     * @param username 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String username) {
        return userMapper.selectUserByUserName(username);
    }

    /**
     * 通过用户ID查询用户
     * 
     * @param id 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long id) {
        return userMapper.selectUserById(id);
    }

    /**
     * 校验用户名称是否唯一
     * 
     * @param username 用户名称
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(String username) {
        int count = userMapper.checkUserNameUnique(username);
        return count == 0;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkPhoneUnique(SysUser user) {
        Long id = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhone());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkEmailUnique(SysUser user) {
        Long id = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getId()) && isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 新增用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        return rows;
    }

    /**
     * 修改用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户状态
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     * 
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String username, String password) {
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(password);
        return userMapper.updateUser(user);
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param id 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long id) {
        return userMapper.deleteUserById(id);
    }

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] ids) {
        for (Long id : ids) {
            SysUser user = new SysUser();
            user.setId(id);
            checkUserAllowed(user);
        }
        return userMapper.deleteUserByIds(ids);
    }

    /**
     * 判断用户数组中是否包含超级管理员
     *
     * @param ids 用户ID数组
     * @return 结果
     */
    @Override
    public boolean hasAdminUser(Long[] ids) {
        for (Long id : ids) {
            SysUser user = selectUserById(id);
            if (user != null && isAdmin()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验用户是否有数据权限
     * 
     * @param id 用户id
     */
    @Override
    public void checkUserDataScope(Long id) {
        if (!isAdmin()) {
            SysUser user = new SysUser();
            user.setId(id);
            List<SysUser> users = selectUserList(user);
            if (users == null || users.isEmpty()) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    /**
     * 修改用户基本信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     * 
     * @param username 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String username, String avatar) {
        return userMapper.updateUserAvatar(username, avatar) > 0;
    }

    /**
     * 用户授权角色
     * 
     * @param id 用户ID
     * @param roleIds 角色组
     */
    @Override
    public void insertUserAuth(Long id, Long[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(id);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 注册用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user) {
        return userMapper.insertUser(user) > 0;
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectAllocatedList(SysUser user) {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 根据用户ID查询用户所属角色组
     * 
     * @param username 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String username) {
        List<SysRole> list = roleMapper.selectRolesByUserName(username);
        if (list == null || list.isEmpty()) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 根据用户ID查询用户所属岗位组
     * 
     * @param username 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String username) {
        List<SysPost> list = postMapper.selectPostsByUserName(username);
        if (list == null || list.isEmpty()) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否支持更新，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (userList == null || userList.isEmpty()) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUsername());
                if (StringUtils.isNull(u)) {
                    // 设置默认密码
                    if (StringUtils.isEmpty(user.getPassword())) {
                        user.setPassword("123456");
                    }
                    // 加密密码
                    user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUsername() + " 导入成功");
                } else if (isUpdateSupport) {
                    // 如果提供了新密码，需要加密
                    if (StringUtils.isNotEmpty(user.getPassword())) {
                        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
                    } else {
                        // 不更新密码
                        user.setPassword(null);
                    }
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUsername() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUsername() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUsername() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
} 