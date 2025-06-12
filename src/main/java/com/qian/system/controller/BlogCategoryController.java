package com.qian.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qian.common.annotation.Log;
import com.qian.system.common.core.controller.BaseController;
import com.qian.common.response.Response;
import com.qian.system.domain.entity.BlogCategory;
import com.qian.common.enums.system.BusinessType;
import com.qian.system.service.IBlogCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.qian.common.utils.poi.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.qian.common.utils.SecurityUtils;

/**
 * 分类管理
 */
@Slf4j
@Tag(name = "分类管理", description = "分类管理相关接口")
@RestController
@RequestMapping("/blog/category")
public class BlogCategoryController extends BaseController {
    @Autowired
    private IBlogCategoryService categoryService;

    /**
     * 获取分类列表
     */
    @Operation(summary = "获取分类列表", description = "根据条件查询分类列表")
    @PreAuthorize("@ss.hasPermi('blog:category:list')")
    @GetMapping("/list")
    public Response<List<BlogCategory>> list(BlogCategory category) {
        List<BlogCategory> list = categoryService.selectCategoryList(category);
        return Response.success(list);
    }

    /**
     * 导出分类列表
     */
    @Operation(summary = "导出分类列表", description = "导出分类列表到Excel")
    @PreAuthorize("@ss.hasPermi('blog:category:export')")
    @Log(title = "分类管理", businessType = "EXPORT")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BlogCategory category) throws IOException {
        List<BlogCategory> list = categoryService.selectCategoryList(category);
        ExcelUtil<BlogCategory> util = new ExcelUtil<>(BlogCategory.class);
        util.exportExcel(response, list, "分类数据");
    }

    /**
     * 根据分类编号获取详细信息
     */
    @Operation(summary = "获取分类详细信息", description = "根据分类ID获取详细信息")
    @PreAuthorize("@ss.hasPermi('blog:category:query')")
    @GetMapping(value = "/{categoryId}")
    public Response<BlogCategory> getInfo(@Parameter(description = "分类ID") @PathVariable Long categoryId) {
        return Response.success(categoryService.selectCategoryById(categoryId));
    }

    /**
     * 新增分类
     */
    @Operation(summary = "新增分类", description = "新增分类信息")
    @PreAuthorize("@ss.hasPermi('blog:category:add')")
    @Log(title = "分类管理", businessType = "INSERT")
    @PostMapping
    public Response<Void> add(@Validated @RequestBody BlogCategory category) {
        category.setCreateBy(SecurityUtils.getUsername());
        return toResponse(categoryService.insertCategory(category));
    }

    /**
     * 修改分类
     */
    @Operation(summary = "修改分类", description = "修改分类信息")
    @PreAuthorize("@ss.hasPermi('blog:category:edit')")
    @Log(title = "分类管理", businessType = "UPDATE")
    @PutMapping
    public Response<Void> edit(@Validated @RequestBody BlogCategory category) {
        category.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(categoryService.updateCategory(category));
    }

    /**
     * 删除分类
     */
    @Operation(summary = "删除分类", description = "删除分类信息")
    @PreAuthorize("@ss.hasPermi('blog:category:remove')")
    @Log(title = "分类管理", businessType = "DELETE")
    @DeleteMapping("/{categoryIds}")
    public Response<Void> remove(@Parameter(description = "分类ID串") @PathVariable Long[] categoryIds) {
        return toResponse(categoryService.deleteCategoryByIds(categoryIds));
    }

    /**
     * 修改分类状态
     */
    @Operation(summary = "修改分类状态", description = "修改分类状态")
    @PreAuthorize("@ss.hasPermi('blog:category:edit')")
    @Log(title = "分类管理", businessType = "UPDATE")
    @PutMapping("/changeStatus")
    public Response<Void> changeStatus(@RequestBody BlogCategory category) {
        category.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(categoryService.updateCategoryStatus(category));
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    private Response<Void> toResponse(int rows) {
        return rows > 0 ? Response.success() : Response.error("操作失败");
    }
} 