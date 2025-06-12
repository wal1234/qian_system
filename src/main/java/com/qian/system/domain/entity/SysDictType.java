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
 * 字典类型表 sys_dict_type
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_type")
@Schema(description = "字典类型表")
public class SysDictType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 字典主键 */
    @TableId(value = "dict_id", type = IdType.AUTO)
    @Schema(description = "字典主键")
    private Long dictId;

    /** 字典名称 */
    @NotBlank(message = "字典名称不能为空")
    @Schema(description = "字典名称")
    private String dictName;

    /** 字典类型 */
    @NotBlank(message = "字典类型不能为空")
    @Schema(description = "字典类型")
    private String dictType;

    /** 状态（0正常 1停用） */
    @NotNull(message = "状态不能为空")
    @Pattern(regexp = "^[01]$", message = "状态只能是0或1")
    @Schema(description = "状态（0正常 1停用）")
    private String status;
} 