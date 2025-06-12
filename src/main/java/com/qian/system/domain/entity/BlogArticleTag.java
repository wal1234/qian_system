package com.qian.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 文章标签关联对象 blog_article_tag
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blog_article_tag")
@Schema(description = "文章标签关联信息")
public class BlogArticleTag extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 文章ID */
    @Schema(description = "文章ID", required = true)
    private Long articleId;

    /** 标签ID */
    @Schema(description = "标签ID", required = true)
    private Long tagId;
} 