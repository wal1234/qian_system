package com.qian.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qian.common.annotation.Log;
import com.qian.system.common.core.controller.BaseController;
import com.qian.common.response.Response;
import com.qian.system.domain.entity.BlogComment;
import com.qian.common.enums.system.BusinessType;
import com.qian.system.service.IBlogCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.qian.common.utils.poi.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.qian.common.utils.SecurityUtils;

/**
 * 评论管理
 */
@Slf4j
@Tag(name = "评论管理", description = "评论管理相关接口")
@RestController
@RequestMapping("/blog/comment")
public class BlogCommentController extends BaseController {
    @Autowired
    private IBlogCommentService commentService;

    /**
     * 获取评论列表
     */
    @Operation(summary = "获取评论列表", description = "根据条件查询评论列表")
    @PreAuthorize("@ss.hasPermi('blog:comment:list')")
    @GetMapping("/list")
    public Response<List<BlogComment>> list(BlogComment comment) {
        List<BlogComment> list = commentService.selectCommentList(comment);
        return Response.success(list);
    }

    /**
     * 导出评论列表
     */
    @Operation(summary = "导出评论列表", description = "导出评论列表到Excel")
    @PreAuthorize("@ss.hasPermi('blog:comment:export')")
    @Log(title = "评论管理", businessType = "EXPORT")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BlogComment comment) throws IOException {
        List<BlogComment> list = commentService.selectCommentList(comment);
        ExcelUtil<BlogComment> util = new ExcelUtil<>(BlogComment.class);
        util.exportExcel(response, list, "评论数据");
    }

    /**
     * 根据评论编号获取详细信息
     */
    @Operation(summary = "获取评论详细信息", description = "根据评论ID获取详细信息")
    @PreAuthorize("@ss.hasPermi('blog:comment:query')")
    @GetMapping(value = "/{commentId}")
    public Response<BlogComment> getInfo(@Parameter(description = "评论ID") @PathVariable Long commentId) {
        return Response.success(commentService.selectCommentById(commentId));
    }

    /**
     * 新增评论
     */
    @Operation(summary = "新增评论", description = "新增评论信息")
    @PreAuthorize("@ss.hasPermi('blog:comment:add')")
    @Log(title = "评论管理", businessType = "INSERT")
    @PostMapping
    public Response<Void> add(@Validated @RequestBody BlogComment comment) {
        comment.setCreateBy(SecurityUtils.getUsername());
        return toResponse(commentService.insertComment(comment));
    }

    /**
     * 修改评论
     */
    @Operation(summary = "修改评论", description = "修改评论信息")
    @PreAuthorize("@ss.hasPermi('blog:comment:edit')")
    @Log(title = "评论管理", businessType = "UPDATE")
    @PutMapping
    public Response<Void> edit(@Validated @RequestBody BlogComment comment) {
        comment.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(commentService.updateComment(comment));
    }

    /**
     * 删除评论
     */
    @Operation(summary = "删除评论", description = "删除评论信息")
    @PreAuthorize("@ss.hasPermi('blog:comment:remove')")
    @Log(title = "评论管理", businessType = "DELETE")
    @DeleteMapping("/{commentIds}")
    public Response<Void> remove(@Parameter(description = "评论ID串") @PathVariable Long[] commentIds) {
        return toResponse(commentService.deleteCommentByIds(commentIds));
    }

    /**
     * 修改评论状态
     */
    @Operation(summary = "修改评论状态", description = "修改评论状态")
    @PreAuthorize("@ss.hasPermi('blog:comment:edit')")
    @Log(title = "评论管理", businessType = "UPDATE")
    @PutMapping("/changeStatus")
    public Response<Void> changeStatus(@RequestBody BlogComment comment) {
        comment.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(commentService.updateCommentStatus(comment));
    }

    /**
     * 点赞评论
     */
    @Operation(summary = "点赞评论", description = "点赞评论")
    @PreAuthorize("@ss.hasPermi('blog:comment:edit')")
    @Log(title = "评论管理", businessType = "UPDATE")
    @PutMapping("/like/{commentId}")
    public Response<Void> like(@Parameter(description = "评论ID") @PathVariable Long commentId) {
        return toResponse(commentService.incrementLikeCount(commentId));
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