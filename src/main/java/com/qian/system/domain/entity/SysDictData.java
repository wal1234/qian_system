package com.qian.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据表 sys_dict_data
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
@Schema(description = "字典数据表")
public class SysDictData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 字典编码 */
    @TableId(value = "dict_code", type = IdType.AUTO)
    @Schema(description = "字典编码")
    private Long dictCode;

    /** 字典排序 */
    @NotNull(message = "字典排序不能为空")
    @Schema(description = "字典排序")
    private Long dictSort;

    /** 字典标签 */
    @NotBlank(message = "字典标签不能为空")
    @Schema(description = "字典标签")
    private String dictLabel;

    /** 字典键值 */
    @NotBlank(message = "字典键值不能为空")
    @Schema(description = "字典键值")
    private String dictValue;

    /** 字典类型 */
    @NotBlank(message = "字典类型不能为空")
    @Schema(description = "字典类型")
    private String dictType;

    /** 样式属性（其他样式扩展） */
    @Schema(description = "样式属性（其他样式扩展）")
    private String cssClass;

    /** 表格回显样式 */
    @Schema(description = "表格回显样式")
    private String listClass;

    /** 是否默认（Y是 N否） */
    @Pattern(regexp = "^[YN]$", message = "是否默认只能是Y或N")
    @Schema(description = "是否默认（Y是 N否）")
    private String isDefault;

    /** 状态（0正常 1停用） */
    @NotNull(message = "状态不能为空")
    @Pattern(regexp = "^[01]$", message = "状态只能是0或1")
    @Schema(description = "状态（0正常 1停用）")
    private String status;
} 