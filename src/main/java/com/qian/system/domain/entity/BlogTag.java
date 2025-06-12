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

/**
 * 标签对象 blog_tag
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blog_tag")
public class BlogTag extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 标签ID */
    @TableId(value = "tag_id", type = IdType.AUTO)
    @Excel(name = "标签ID", cellType = ColumnType.NUMERIC)
    private Long tagId;

    /** 标签名称 */
    @NotBlank(message = "标签名称不能为空")
    @Excel(name = "标签名称")
    private String tagName;

    /** 标签描述 */
    @Excel(name = "标签描述")
    private String description;

    /** 显示顺序 */
    @NotNull(message = "显示顺序不能为空")
    @Excel(name = "显示顺序", cellType = ColumnType.NUMERIC)
    private Integer orderNum;

    /** 状态（0正常 1停用） */
    @NotNull(message = "状态不能为空")
    @Pattern(regexp = "^[01]$", message = "状态只能是0或1")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Pattern(regexp = "^[01]$", message = "删除标志只能是0或1")
    private String delFlag;
} 