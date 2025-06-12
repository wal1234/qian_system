package com.qian.system.service;

import java.util.List;
import java.util.Set;

import com.qian.system.domain.entity.SysRole;

/**
 * 角色业务层
 */
public interface ISysRoleService {
    /**
     * 根据条件分页查询角色数据
     * 
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SysRole> selectRolesByUserId(Long userId);

    /**
     * 通过角色ID查询角色
     * 
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    public SysRole selectRoleById(Long roleId);

    /**
     * 校验角色名称是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    public boolean checkRoleNameUnique(SysRole role);

    /**
     * 校验角色权限是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    public boolean checkRoleKeyUnique(SysRole role);

    /**
     * 校验角色是否允许操作
     * 
     * @param role 角色信息
     */
    public void checkRoleAllowed(SysRole role);

    /**
     * 校验角色是否有数据权限
     * 
     * @param roleId 角色id
     */
    public void checkRoleDataScope(Long roleId);

    /**
     * 通过角色ID查询角色使用数量
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * 新增保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(SysRole role);

    /**
     * 修改保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(SysRole role);

    /**
     * 修改角色状态
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int updateRoleStatus(SysRole role);

    /**
     * 修改数据权限信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int authDataScope(SysRole role);

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleById(Long roleId);

    /**
     * 批量删除角色信息
     * 
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    public int deleteRoleByIds(Long[] roleIds);

    /**
     * 获取角色选择框列表
     * 
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    public List<Long> selectRoleListByUserId(Long userId);

    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    public List<SysRole> selectRoleAll();

    /**
     * 查询角色权限
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    public Set<String> selectRolePermissions(Long roleId);

    /**
     * 批量修改角色状态
     * 
     * @param roleIds 角色ID数组
     * @param status 角色状态
     * @return 结果
     */
    public int updateRoleStatusBatch(Long[] roleIds, String status);

    /**
     * 批量修改角色数据权限
     * 
     * @param roleIds 角色ID数组
     * @param dataScope 数据权限
     * @return 结果
     */
    public int updateRoleDataScopeBatch(Long[] roleIds, String dataScope);

    /**
     * 导入角色数据
     * 
     * @param roleList 角色数据列表
     * @param updateSupport 是否支持更新
     * @param operName 操作用户
     * @return 结果
     */
    public String importRole(List<SysRole> roleList, Boolean updateSupport, String operName);
} 