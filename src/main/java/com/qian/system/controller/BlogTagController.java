package com.qian.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qian.common.annotation.Log;
import com.qian.system.common.core.controller.BaseController;
import com.qian.common.response.Response;
import com.qian.system.domain.entity.BlogTag;
import com.qian.common.enums.system.BusinessType;
import com.qian.system.service.IBlogTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.qian.common.utils.poi.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.qian.common.utils.SecurityUtils;

/**
 * 标签管理
 */
@Slf4j
@Tag(name = "标签管理", description = "标签管理相关接口")
@RestController
@RequestMapping("/blog/tag")
public class BlogTagController extends BaseController {
    @Autowired
    private IBlogTagService tagService;

    /**
     * 获取标签列表
     */
    @Operation(summary = "获取标签列表", description = "根据条件查询标签列表")
    @PreAuthorize("@ss.hasPermi('blog:tag:list')")
    @GetMapping("/list")
    public Response<List<BlogTag>> list(BlogTag tag) {
        List<BlogTag> list = tagService.selectTagList(tag);
        return Response.success(list);
    }

    /**
     * 导出标签列表
     */
    @Operation(summary = "导出标签列表", description = "导出标签列表到Excel")
    @PreAuthorize("@ss.hasPermi('blog:tag:export')")
    @Log(title = "标签管理", businessType = "EXPORT")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BlogTag tag) throws IOException {
        List<BlogTag> list = tagService.selectTagList(tag);
        ExcelUtil<BlogTag> util = new ExcelUtil<>(BlogTag.class);
        util.exportExcel(response, list, "标签数据");
    }

    /**
     * 根据标签编号获取详细信息
     */
    @Operation(summary = "获取标签详细信息", description = "根据标签ID获取详细信息")
    @PreAuthorize("@ss.hasPermi('blog:tag:query')")
    @GetMapping(value = "/{tagId}")
    public Response<BlogTag> getInfo(@Parameter(description = "标签ID") @PathVariable Long tagId) {
        return Response.success(tagService.selectTagById(tagId));
    }

    /**
     * 新增标签
     */
    @Operation(summary = "新增标签", description = "新增标签信息")
    @PreAuthorize("@ss.hasPermi('blog:tag:add')")
    @Log(title = "标签管理", businessType = "INSERT")
    @PostMapping
    public Response<Void> add(@Validated @RequestBody BlogTag tag) {
        tag.setCreateBy(SecurityUtils.getUsername());
        return toResponse(tagService.insertTag(tag));
    }

    /**
     * 修改标签
     */
    @Operation(summary = "修改标签", description = "修改标签信息")
    @PreAuthorize("@ss.hasPermi('blog:tag:edit')")
    @Log(title = "标签管理", businessType = "UPDATE")
    @PutMapping
    public Response<Void> edit(@Validated @RequestBody BlogTag tag) {
        tag.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(tagService.updateTag(tag));
    }

    /**
     * 删除标签
     */
    @Operation(summary = "删除标签", description = "删除标签信息")
    @PreAuthorize("@ss.hasPermi('blog:tag:remove')")
    @Log(title = "标签管理", businessType = "DELETE")
    @DeleteMapping("/{tagIds}")
    public Response<Void> remove(@Parameter(description = "标签ID串") @PathVariable Long[] tagIds) {
        return toResponse(tagService.deleteTagByIds(tagIds));
    }

    /**
     * 修改标签状态
     */
    @Operation(summary = "修改标签状态", description = "修改标签状态")
    @PreAuthorize("@ss.hasPermi('blog:tag:edit')")
    @Log(title = "标签管理", businessType = "UPDATE")
    @PutMapping("/changeStatus")
    public Response<Void> changeStatus(@RequestBody BlogTag tag) {
        tag.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(tagService.updateTagStatus(tag));
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