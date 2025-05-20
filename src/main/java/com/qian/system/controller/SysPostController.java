package com.qian.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qian.common.annotation.Log;
import com.qian.system.common.core.controller.BaseController;
import com.qian.common.response.Response;
import com.qian.common.enums.system.BusinessType;
import com.qian.system.common.utils.poi.ExcelUtil;
import com.qian.system.domain.SysPost;
import com.qian.system.service.ISysPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 岗位信息操作处理
 */
@Tag(name = "岗位管理")
@RestController
@RequestMapping("/system/post")
public class SysPostController extends BaseController {
    @Autowired
    private ISysPostService postService;

    @Operation(summary = "获取岗位列表")
    @Parameters({
        @Parameter(name = "postName", description = "岗位名称"),
        @Parameter(name = "postCode", description = "岗位编码"),
        @Parameter(name = "status", description = "状态")
    })
    @PreAuthorize("@ss.hasPermi('system:post:list')")
    @GetMapping("/list")
    public Response<List<SysPost>> list(SysPost post) {
        List<SysPost> list = postService.selectPostList(post);
        return Response.success(list);
    }

    @Operation(summary = "导出岗位列表")
    @PreAuthorize("@ss.hasPermi('system:post:export')")
    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Response<String> export(SysPost post) {
        List<SysPost> list = postService.selectPostList(post);
        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
        return util.exportExcel(list, "岗位数据");
    }

    @Operation(summary = "根据岗位编号获取详细信息")
    @Parameter(name = "postId", description = "岗位编号", required = true)
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @GetMapping(value = "/{postId}")
    public Response<SysPost> getInfo(@PathVariable Long postId) {
        return Response.success(postService.selectPostById(postId));
    }

    @Operation(summary = "新增岗位")
    @Parameter(name = "post", description = "岗位信息", required = true)
    @PreAuthorize("@ss.hasPermi('system:post:add')")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Response<Void> add(@Validated @RequestBody SysPost post) {
        if (!postService.checkPostNameUnique(post)) {
            return Response.error(500, "新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (!postService.checkPostCodeUnique(post)) {
            return Response.error(500, "新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setCreateBy(getUsername());
        return toResponse(postService.insertPost(post));
    }

    @Operation(summary = "修改岗位")
    @Parameter(name = "post", description = "岗位信息", required = true)
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Response<Void> edit(@Validated @RequestBody SysPost post) {
        if (!postService.checkPostNameUnique(post)) {
            return Response.error(500, "修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (!postService.checkPostCodeUnique(post)) {
            return Response.error(500, "修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setUpdateBy(getUsername());
        return toResponse(postService.updatePost(post));
    }

    @Operation(summary = "删除岗位")
    @Parameter(name = "postId", description = "岗位编号", required = true)
    @PreAuthorize("@ss.hasPermi('system:post:remove')")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{postId}")
    public Response<Void> remove(@PathVariable Long postId) {
        if (postService.countUserPostById(postId) > 0) {
            return Response.error(500, "岗位存在用户,不允许删除");
        }
        return toResponse(postService.deletePostById(postId));
    }

    /**
     * 将操作结果转换为Response
     */
    private Response<Void> toResponse(int rows) {
        return rows > 0 ? Response.success() : Response.error(500, "操作失败");
    }
} 