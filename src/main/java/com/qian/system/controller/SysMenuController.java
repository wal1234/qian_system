package com.qian.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qian.common.annotation.Log;
import com.qian.common.response.Response;
import com.qian.system.domain.entity.SysMenu;
import com.qian.system.service.ISysMenuService;
import com.qian.common.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 菜单权限 控制器
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("/system/menu")
public class SysMenuController {
    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @Operation(summary = "获取菜单列表")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public Response<List<SysMenu>> list(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(SecurityUtils.getUserId());
        return Response.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @Operation(summary = "根据菜单编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public Response<SysMenu> getInfo(@PathVariable Long menuId) {
        return Response.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @Operation(summary = "获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public Response<List<SysMenu>> treeselect(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(SecurityUtils.getUserId());
        return Response.success(menuService.buildMenuTree(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @Operation(summary = "加载对应角色菜单列表树")
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public Response<List<SysMenu>> roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(SecurityUtils.getUserId());
        return Response.success(menuService.buildMenuTree(menus));
    }

    /**
     * 新增菜单
     */
    @Operation(summary = "新增菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "菜单管理", businessType = "新增")
    @PostMapping
    public Response<Boolean> add(@Validated @RequestBody SysMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return Response.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        return Response.success(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @Operation(summary = "修改菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "菜单管理", businessType = "修改")
    @PutMapping
    public Response<Boolean> edit(@Validated @RequestBody SysMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return Response.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        if (menu.getMenuId().equals(menu.getParentId())) {
            return Response.error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        return Response.success(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @Operation(summary = "删除菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "菜单管理", businessType = "删除")
    @DeleteMapping("/{menuId}")
    public Response<Boolean> remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return Response.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return Response.error("菜单已分配,不允许删除");
        }
        return Response.success(menuService.deleteMenuById(menuId));
    }
} 