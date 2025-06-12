package com.qian.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qian.common.annotation.Excel;
import com.qian.common.annotation.Excel.ColumnType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 文章对象 blog_article
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blog_article")
@Schema(description = "文章信息")
public class BlogArticle extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 文章ID */
    @TableId(value = "article_id", type = IdType.AUTO)
    @Excel(name = "文章ID", cellType = ColumnType.NUMERIC)
    @Schema(description = "文章ID")
    private Long articleId;

    /** 文章标题 */
    @NotBlank(message = "文章标题不能为空")
    @Size(min = 2, max = 100, message = "文章标题长度必须在2到100个字符之间")
    @Excel(name = "文章标题")
    @Schema(description = "文章标题", required = true)
    private String title;

    /** 文章内容 */
    @NotBlank(message = "文章内容不能为空")
    @Schema(description = "文章内容", required = true)
    private String content;

    /** 文章摘要 */
    @Size(max = 500, message = "文章摘要长度不能超过500个字符")
    @Excel(name = "文章摘要")
    @Schema(description = "文章摘要")
    private String summary;

    /** 封面图片 */
    @Size(max = 255, message = "封面图片地址长度不能超过255个字符")
    @Schema(description = "封面图片")
    private String coverImage;

    /** 分类ID */
    @NotNull(message = "分类ID不能为空")
    @Excel(name = "分类ID", cellType = ColumnType.NUMERIC)
    @Schema(description = "分类ID", required = true)
    private Long categoryId;

    /** 浏览量 */
    @Excel(name = "浏览量", cellType = ColumnType.NUMERIC)
    @Schema(description = "浏览量")
    private Integer viewCount;

    /** 点赞数 */
    @Excel(name = "点赞数", cellType = ColumnType.NUMERIC)
    @Schema(description = "点赞数")
    private Integer likeCount;

    /** 评论数 */
    @Excel(name = "评论数", cellType = ColumnType.NUMERIC)
    @Schema(description = "评论数")
    private Integer commentCount;

    /** 状态（0草稿 1发布） */
    @NotNull(message = "状态不能为空")
    @Pattern(regexp = "^[01]$", message = "状态只能是0或1")
    @Excel(name = "状态", readConverterExp = "0=草稿,1=发布")
    @Schema(description = "状态（0草稿 1发布）", required = true)
    private Integer status;

    /** 是否置顶（0否 1是） */
    @NotNull(message = "是否置顶不能为空")
    @Pattern(regexp = "^[01]$", message = "是否置顶只能是0或1")
    @Excel(name = "是否置顶", readConverterExp = "0=否,1=是")
    @Schema(description = "是否置顶（0否 1是）", required = true)
    private Integer isTop;

    /** 是否推荐（0否 1是） */
    @NotNull(message = "是否推荐不能为空")
    @Pattern(regexp = "^[01]$", message = "是否推荐只能是0或1")
    @Excel(name = "是否推荐", readConverterExp = "0=否,1=是")
    @Schema(description = "是否推荐（0否 1是）", required = true)
    private Integer isRecommend;

    /** 删除标志（0代表存在 1代表删除） */
    @Pattern(regexp = "^[01]$", message = "删除标志只能是0或1")
    @Schema(description = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

    /** 分类名称 */
    @TableField(exist = false)
    @Excel(name = "分类名称")
    @Schema(description = "分类名称")
    private String categoryName;

    /** 标签列表 */
    @TableField(exist = false)
    @Schema(description = "标签列表")
    private Long[] tagIds;
} 