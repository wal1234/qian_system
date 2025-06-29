package com.qian.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qian.common.annotation.Excel;
import com.qian.common.annotation.Excel.ColumnType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 分类对象 blog_category
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blog_category")
@Schema(description = "分类信息")
public class BlogCategory extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    @TableId(value = "category_id", type = IdType.AUTO)
    @Excel(name = "分类ID", cellType = ColumnType.NUMERIC)
    @Schema(description = "分类ID")
    private Long categoryId;

    /** 分类名称 */
    @NotBlank(message = "分类名称不能为空")
    @Excel(name = "分类名称")
    @Schema(description = "分类名称")
    private String categoryName;

    /** 分类描述 */
    @Excel(name = "分类描述")
    @Schema(description = "分类描述")
    private String description;

    /** 显示顺序 */
    @NotNull(message = "显示顺序不能为空")
    @Excel(name = "显示顺序", cellType = ColumnType.NUMERIC)
    @Schema(description = "显示顺序")
    private Integer orderNum;

    /** 状态（0正常 1停用） */
    @NotNull(message = "状态不能为空")
    @Pattern(regexp = "^[01]$", message = "状态只能是0或1")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    @Schema(description = "状态（0正常 1停用）")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Pattern(regexp = "^[01]$", message = "删除标志只能是0或1")
    @Schema(description = "删除标志（0代表存在 1代表删除）")
    private String delFlag;
} 