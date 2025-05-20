package com.qian.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qian.common.annotation.Log;
import com.qian.system.common.core.controller.BaseController;
import com.qian.system.domain.TreeSelect;
import com.qian.common.enums.system.BusinessType;
import com.qian.common.response.Response;
import com.qian.system.domain.SysDept;
import com.qian.system.service.ISysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 部门信息
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {
    @Autowired
    private ISysDeptService deptService;

    @Operation(summary = "获取部门列表")
    @Parameters({
        @Parameter(name = "deptName", description = "部门名称"),
        @Parameter(name = "status", description = "状态")
    })
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public Response<List<SysDept>> list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return Response.success(depts);
    }

    @Operation(summary = "根据部门编号获取详细信息")
    @Parameter(name = "deptId", description = "部门编号", required = true)
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public Response<SysDept> getInfo(@PathVariable Long deptId) {
        return Response.success(deptService.selectDeptById(deptId));
    }

    @Operation(summary = "获取部门下拉树列表")
    @Parameters({
        @Parameter(name = "deptName", description = "部门名称"),
        @Parameter(name = "status", description = "状态")
    })
    @GetMapping("/treeselect")
    public Response<List<TreeSelect>> treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return Response.success(deptService.buildDeptTreeSelect(depts));
    }

    @Operation(summary = "获取角色部门下拉树列表")
    @Parameter(name = "roleId", description = "角色ID", required = true)
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public Response<Object> roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        return Response.success(deptService.buildDeptTreeSelect(depts));
    }

    @Operation(summary = "新增部门")
    @Parameter(name = "dept", description = "部门信息", required = true)
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Response<Void> add(@Validated @RequestBody SysDept dept) {
        if (!deptService.checkDeptNameUnique(dept)) {
            return Response.error(500, "新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(getUsername());
        return toResponse(deptService.insertDept(dept));
    }

    @Operation(summary = "修改部门")
    @Parameter(name = "dept", description = "部门信息", required = true)
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Response<Void> edit(@Validated @RequestBody SysDept dept) {
        if (!deptService.checkDeptNameUnique(dept)) {
            return Response.error(500, "修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(dept.getDeptId())) {
            return Response.error(500, "修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        dept.setUpdateBy(getUsername());
        return toResponse(deptService.updateDept(dept));
    }

    @Operation(summary = "删除部门")
    @Parameter(name = "deptId", description = "部门编号", required = true)
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public Response<Void> remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return Response.error(500, "存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return Response.error(500, "部门存在用户,不允许删除");
        }
        return toResponse(deptService.deleteDeptById(deptId));
    }

    /**
     * 将操作结果转换为Response
     */
    private Response<Void> toResponse(int rows) {
        return rows > 0 ? Response.success() : Response.error(500, "操作失败");
    }
} 