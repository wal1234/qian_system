package com.qian.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qian.system.common.exception.ServiceException;
import com.qian.system.common.utils.StringUtils;
import com.qian.system.domain.entity.SysRole;
import com.qian.system.domain.entity.SysRoleDept;
import com.qian.system.domain.entity.SysRoleMenu;
import com.qian.system.domain.entity.LoginUser;
import com.qian.system.mapper.SysRoleMapper;
import com.qian.system.mapper.SysRoleDeptMapper;
import com.qian.system.mapper.SysRoleMenuMapper;
import com.qian.system.mapper.SysUserRoleMapper;
import com.qian.system.service.ISysRoleService;
import com.qian.system.service.TokenService;
import com.qian.common.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;

/**
 * 角色 业务层处理
 */
@Slf4j
@Service
public class SysRoleServiceImpl implements ISysRoleService {
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysRoleDeptMapper roleDeptMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;
    
    private static final String ROLE_CACHE_KEY = "role:";
    private static final String ROLE_PERMISSIONS_CACHE_KEY = "role:permissions:";

    /**
     * 根据条件分页查询角色数据
     * 
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role);
    }

    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        List<SysRole> userRoles = roleMapper.selectRolesByUserId(userId);
        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles) {
            for (SysRole userRole : userRoles) {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return selectRoleList(new SysRole());
    }

    /**
     * 通过角色ID查询角色
     * 
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * 校验角色名称是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 校验角色权限是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleKeyUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 校验角色是否允许操作
     * 
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }

    /**
     * 校验角色是否有数据权限
     * 
     * @param roleId 角色id
     */
    @Override
    public void checkRoleDataScope(Long roleId) {
        if (!isAdmin()) {
            SysRole role = new SysRole();
            role.setRoleId(roleId);
            List<SysRole> roles = selectRoleList(role);
            if (roles == null || roles.isEmpty()) {
                throw new ServiceException("没有权限访问角色数据！");
            }
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 新增保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(SysRole role) {
        // 新增角色信息
        roleMapper.insertRole(role);
        // 新增角色菜单关联
        insertRoleMenu(role);
        // 新增角色部门关联
        insertRoleDept(role);
        return 1;
    }

    /**
     * 修改保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(SysRole role) {
        // 修改角色信息
        roleMapper.updateRole(role);
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        // 新增角色菜单关联
        insertRoleMenu(role);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
        // 新增角色部门关联
        insertRoleDept(role);
        // 清除角色权限缓存
        clearRolePermissionsCache(role.getRoleId());
        return 1;
    }

    /**
     * 新增角色菜单信息
     * 
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * 新增角色部门信息
     * 
     * @param role 角色对象
     */
    public int insertRoleDept(SysRole role) {
        int rows = 1;
        // 新增角色与部门管理
        List<SysRoleDept> list = new ArrayList<SysRoleDept>();
        for (Long deptId : role.getDeptIds()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0) {
            rows = roleDeptMapper.batchRoleDept(list);
        }
        return rows;
    }

    /**
     * 修改角色状态
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(SysRole role) {
        return roleMapper.updateRole(role);
    }

    /**
     * 修改数据权限信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role) {
        // 修改角色信息
        roleMapper.updateRole(role);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
        // 新增角色部门关联
        insertRoleDept(role);
        return 1;
    }

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleById(Long roleId) {
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(roleId);
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * 批量删除角色信息
     * 
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRole(roleId));
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        for (Long roleId : roleIds) {
            roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        }
        // 删除角色与部门关联
        for (Long roleId : roleIds) {
            roleDeptMapper.deleteRoleDeptByRoleId(roleId);
        }
        return roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * 获取角色选择框列表
     * 
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return roleMapper.selectRoleListByUserId(userId);
    }

    /**
     * 判断是否是管理员
     */
    private boolean isAdmin() {
        LoginUser loginUser = tokenService.getLoginUser();
        return loginUser != null && loginUser.getUser() != null && loginUser.getUser().getId() == 1L;
    }

    /**
     * 获取角色权限缓存
     */
    private Set<String> getRolePermissionsCache(Long roleId) {
        String cacheKey = ROLE_PERMISSIONS_CACHE_KEY + roleId;
        Set<String> permissions = redisCache.getCacheObject(cacheKey);
        if (permissions == null) {
            permissions = roleMapper.selectRolePermissions(roleId);
            redisCache.setCacheObject(cacheKey, permissions, 30, TimeUnit.MINUTES);
        }
        return permissions;
    }
    
    /**
     * 清除角色权限缓存
     */
    private void clearRolePermissionsCache(Long roleId) {
        String cacheKey = ROLE_PERMISSIONS_CACHE_KEY + roleId;
        redisCache.deleteObject(cacheKey);
    }
    
    /**
     * 清除所有角色权限缓存
     */
    private void clearAllRolePermissionsCache() {
        redisCache.deleteObject(ROLE_PERMISSIONS_CACHE_KEY + "*");
    }
    
    @Override
    public Set<String> selectRolePermissions(Long roleId) {
        return getRolePermissionsCache(roleId);
    }

    /**
     * 批量修改角色状态
     */
    @Override
    @Transactional
    public int updateRoleStatusBatch(Long[] roleIds, String status) {
        int rows = 0;
        for (Long roleId : roleIds) {
            SysRole role = new SysRole();
            role.setRoleId(roleId);
            role.setStatus(status);
            rows += roleMapper.updateRole(role);
            // 清除角色权限缓存
            clearRolePermissionsCache(roleId);
        }
        return rows;
    }

    /**
     * 批量修改角色数据权限
     */
    @Override
    @Transactional
    public int updateRoleDataScopeBatch(Long[] roleIds, String dataScope) {
        int rows = 0;
        for (Long roleId : roleIds) {
            SysRole role = new SysRole();
            role.setRoleId(roleId);
            role.setDataScope(dataScope);
            rows += roleMapper.updateRole(role);
            // 清除角色权限缓存
            clearRolePermissionsCache(roleId);
        }
        return rows;
    }

    /**
     * 导入角色数据
     */
    @Override
    @Transactional
    public String importRole(List<SysRole> roleList, Boolean updateSupport, String operName) {
        if (StringUtils.isNull(roleList) || roleList.size() == 0) {
            throw new ServiceException("导入角色数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysRole role : roleList) {
            try {
                // 验证是否存在这个角色
                SysRole r = roleMapper.checkRoleNameUnique(role.getRoleName());
                if (StringUtils.isNull(r)) {
                    role.setCreateBy(operName);
                    this.insertRole(role);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、角色 " + role.getRoleName() + " 导入成功");
                } else if (updateSupport) {
                    role.setRoleId(r.getRoleId());
                    role.setUpdateBy(operName);
                    this.updateRole(role);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、角色 " + role.getRoleName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、角色 " + role.getRoleName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、角色 " + role.getRoleName() + " 导入失败：";
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