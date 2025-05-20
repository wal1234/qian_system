package com.qian.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.qian.system.domain.SysRoleDept;

/**
 * 角色与部门关联表 数据层
 */
@Mapper
public interface SysRoleDeptMapper {
    /**
     * 通过角色ID删除角色和部门关联
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteRoleDeptByRoleId(Long roleId);

    /**
     * 批量新增角色部门信息
     * 
     * @param roleDeptList 角色部门列表
     * @return 结果
     */
    int batchRoleDept(List<SysRoleDept> roleDeptList);

    /**
     * 通过角色ID查询部门ID
     * 
     * @param roleId 角色ID
     * @return 部门ID列表
     */
    List<Long> selectDeptIdsByRoleId(Long roleId);
} 