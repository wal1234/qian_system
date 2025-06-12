package com.qian.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qian.common.annotation.Log;
import com.qian.system.common.core.controller.BaseController;
import com.qian.common.response.Response;
import com.qian.system.domain.entity.BlogArticle;
import com.qian.common.enums.system.BusinessType;
import com.qian.system.service.IBlogArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.qian.common.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.qian.common.utils.SecurityUtils;

/**
 * 文章管理
 */
@Slf4j
@Tag(name = "文章管理", description = "文章管理相关接口")
@RestController
@RequestMapping("/blog/article")
public class BlogArticleController extends BaseController {
    @Autowired
    private IBlogArticleService articleService;

    /**
     * 获取文章列表
     */
    @Operation(summary = "获取文章列表", description = "根据条件分页查询文章列表")
    @PreAuthorize("@ss.hasPermi('blog:article:list')")
    @GetMapping("/list")
    public Response<List<BlogArticle>> list(BlogArticle article) {
        List<BlogArticle> list = articleService.selectArticleList(article);
        return Response.success(list);
    }

    /**
     * 导出文章列表
     */
    @Operation(summary = "导出文章列表", description = "导出文章列表到Excel")
    @PreAuthorize("@ss.hasPermi('blog:article:export')")
    @Log(title = "文章管理", businessType = "EXPORT")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BlogArticle article) throws IOException {
        List<BlogArticle> list = articleService.selectArticleList(article);
        ExcelUtil<BlogArticle> util = new ExcelUtil<>(BlogArticle.class);
        util.exportExcel(response, list, "文章数据");
    }

    /**
     * 根据文章编号获取详细信息
     */
    @Operation(summary = "获取文章详细信息", description = "根据文章ID获取详细信息")
    @PreAuthorize("@ss.hasPermi('blog:article:query')")
    @GetMapping(value = "/{articleId}")
    public Response<BlogArticle> getInfo(@Parameter(description = "文章ID") @PathVariable Long articleId) {
        return Response.success(articleService.selectArticleById(articleId));
    }

    /**
     * 新增文章
     */
    @Operation(summary = "新增文章", description = "新增文章信息")
    @PreAuthorize("@ss.hasPermi('blog:article:add')")
    @Log(title = "文章管理", businessType = "INSERT")
    @PostMapping
    public Response<Void> add(@Validated @RequestBody BlogArticle article) {
        article.setCreateBy(SecurityUtils.getUsername());
        return toResponse(articleService.insertArticle(article));
    }

    /**
     * 修改文章
     */
    @Operation(summary = "修改文章", description = "修改文章信息")
    @PreAuthorize("@ss.hasPermi('blog:article:edit')")
    @Log(title = "文章管理", businessType = "UPDATE")
    @PutMapping
    public Response<Void> edit(@Validated @RequestBody BlogArticle article) {
        article.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(articleService.updateArticle(article));
    }

    /**
     * 删除文章
     */
    @Operation(summary = "删除文章", description = "删除文章信息")
    @PreAuthorize("@ss.hasPermi('blog:article:remove')")
    @Log(title = "文章管理", businessType = "DELETE")
    @DeleteMapping("/{articleIds}")
    public Response<Void> remove(@Parameter(description = "文章ID串") @PathVariable Long[] articleIds) {
        return toResponse(articleService.deleteArticleByIds(articleIds));
    }

    /**
     * 修改文章状态
     */
    @Operation(summary = "修改文章状态", description = "修改文章状态")
    @PreAuthorize("@ss.hasPermi('blog:article:edit')")
    @Log(title = "文章管理", businessType = "UPDATE")
    @PutMapping("/changeStatus")
    public Response<Void> changeStatus(@RequestBody BlogArticle article) {
        article.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(articleService.updateArticleStatus(article));
    }

    /**
     * 修改文章置顶状态
     */
    @Operation(summary = "修改文章置顶状态", description = "修改文章置顶状态")
    @PreAuthorize("@ss.hasPermi('blog:article:edit')")
    @Log(title = "文章管理", businessType = "UPDATE")
    @PutMapping("/changeTop")
    public Response<Void> changeTop(@RequestBody BlogArticle article) {
        article.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(articleService.updateArticleTop(article));
    }

    /**
     * 修改文章推荐状态
     */
    @Operation(summary = "修改文章推荐状态", description = "修改文章推荐状态")
    @PreAuthorize("@ss.hasPermi('blog:article:edit')")
    @Log(title = "文章管理", businessType = "UPDATE")
    @PutMapping("/changeRecommend")
    public Response<Void> changeRecommend(@RequestBody BlogArticle article) {
        article.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(articleService.updateArticleRecommend(article));
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