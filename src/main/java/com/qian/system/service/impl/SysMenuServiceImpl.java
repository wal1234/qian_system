package com.qian.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qian.common.exception.ServiceException;
import com.qian.common.utils.SecurityUtils;
import com.qian.common.utils.StringUtils;
import com.qian.system.domain.entity.SysMenu;
import com.qian.system.mapper.SysMenuMapper;
import com.qian.system.service.ISysMenuService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单权限 服务实现
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    /**
     * 根据用户ID查询权限
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = baseMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询菜单树信息
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = baseMapper.selectMenuTreeByUserId(userId);
        return getChildPerms(menus, 0);
    }

    /**
     * 根据角色ID查询菜单树信息
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return baseMapper.selectMenuListByRoleId(roleId);
    }

    /**
     * 构建前端所需要的菜单
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<>();
        List<Long> tempList = menus.stream().map(SysMenu::getMenuId).collect(Collectors.toList());
        for (SysMenu menu : menus) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 构建前端路由所需要的菜单
     */
    @Override
    public List<SysMenu> buildMenus(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (menu.getParentId() == 0) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        return returnList;
    }

    /**
     * 根据菜单ID查询信息
     */
    @Override
    public SysMenu selectMenuById(Long menuId) {
        return baseMapper.selectMenuById(menuId);
    }

    /**
     * 是否存在菜单子节点
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        return baseMapper.hasChildByMenuId(menuId) > 0;
    }

    /**
     * 查询菜单是否存在角色
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        return baseMapper.checkMenuExistRole(menuId) > 0;
    }

    /**
     * 新增保存菜单信息
     */
    @Override
    public boolean insertMenu(SysMenu menu) {
        menu.setCreateBy(SecurityUtils.getUsername());
        return save(menu);
    }

    /**
     * 修改保存菜单信息
     */
    @Override
    public boolean updateMenu(SysMenu menu) {
        menu.setUpdateBy(SecurityUtils.getUsername());
        return updateById(menu);
    }

    /**
     * 删除菜单管理信息
     */
    @Override
    public boolean deleteMenuById(Long menuId) {
        if (hasChildByMenuId(menuId)) {
            throw new ServiceException("存在子菜单,不允许删除");
        }
        if (checkMenuExistRole(menuId)) {
            throw new ServiceException("菜单已分配,不允许删除");
        }
        return removeById(menuId);
    }

    /**
     * 校验菜单名称是否唯一
     */
    @Override
    public boolean checkMenuNameUnique(SysMenu menu) {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu info = baseMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 根据父节点的ID获取所有子节点
     */
    private List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<>();
        for (SysMenu t : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<>();
        for (SysMenu n : list) {
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }
} 