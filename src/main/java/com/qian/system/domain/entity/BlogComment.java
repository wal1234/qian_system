package com.qian.system.domain.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qian.common.annotation.Excel;
import com.qian.common.annotation.Excel.ColumnType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 评论对象 blog_comment
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blog_comment")
@Schema(description = "评论信息")
public class BlogComment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 评论ID */
    @TableId(value = "comment_id", type = IdType.AUTO)
    @Excel(name = "评论ID", cellType = ColumnType.NUMERIC)
    @Schema(description = "评论ID")
    private Long commentId;

    /** 文章ID */
    @NotNull(message = "文章ID不能为空")
    @Excel(name = "文章ID", cellType = ColumnType.NUMERIC)
    @Schema(description = "文章ID", required = true)
    private Long articleId;

    /** 父评论ID */
    @Excel(name = "父评论ID", cellType = ColumnType.NUMERIC)
    @Schema(description = "父评论ID")
    private Long parentId;

    /** 评论用户ID */
    @Excel(name = "评论用户ID", cellType = ColumnType.NUMERIC)
    @Schema(description = "评论用户ID")
    private Long userId;

    /** 评论内容 */
    @NotBlank(message = "评论内容不能为空")
    @Size(min = 1, max = 1000, message = "评论内容长度必须在1到1000个字符之间")
    @Excel(name = "评论内容")
    @Schema(description = "评论内容", required = true)
    private String content;

    /** 点赞数 */
    @Excel(name = "点赞数", cellType = ColumnType.NUMERIC)
    @Schema(description = "点赞数")
    private Integer likeCount;

    /** 状态（0隐藏 1显示） */
    @NotNull(message = "状态不能为空")
    @Pattern(regexp = "^[01]$", message = "状态只能是0或1")
    @Excel(name = "状态", readConverterExp = "0=隐藏,1=显示")
    @Schema(description = "状态（0隐藏 1显示）", required = true)
    private Integer status;

    /** 删除标志（0代表存在 1代表删除） */
    @Pattern(regexp = "^[01]$", message = "删除标志只能是0或1")
    @Schema(description = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

    /** 文章标题 */
    @TableField(exist = false)
    @Excel(name = "文章标题")
    @Schema(description = "文章标题")
    private String articleTitle;

    /** 评论用户昵称 */
    @TableField(exist = false)
    @Excel(name = "评论用户昵称")
    @Schema(description = "评论用户昵称")
    private String nickName;

    /** 评论人昵称 */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    @Excel(name = "评论人昵称")
    @Schema(description = "评论人昵称")
    private String nickname;

    /** 评论人邮箱 */
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    @Excel(name = "评论人邮箱")
    @Schema(description = "评论人邮箱")
    private String email;

    /** 评论人网站 */
    @Size(max = 100, message = "网站地址长度不能超过100个字符")
    @Excel(name = "评论人网站")
    @Schema(description = "评论人网站")
    private String website;

    /** 评论时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "评论时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "评论时间")
    private LocalDateTime commentTime;
} 