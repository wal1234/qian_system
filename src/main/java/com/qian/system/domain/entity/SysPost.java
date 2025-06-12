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
 * 岗位表 sys_post
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_post")
@Schema(description = "岗位表")
public class SysPost extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 岗位序号 */
    @TableId(value = "post_id", type = IdType.AUTO)
    @Schema(description = "岗位序号")
    private Long postId;

    /** 岗位编码 */
    @NotBlank(message = "岗位编码不能为空")
    @Schema(description = "岗位编码")
    private String postCode;

    /** 岗位名称 */
    @NotBlank(message = "岗位名称不能为空")
    @Schema(description = "岗位名称")
    private String postName;

    /** 岗位排序 */
    @NotNull(message = "岗位排序不能为空")
    @Schema(description = "岗位排序")
    private Integer postSort;

    /** 状态（0正常 1停用） */
    @NotNull(message = "状态不能为空")
    @Pattern(regexp = "^[01]$", message = "状态只能是0或1")
    @Schema(description = "状态（0正常 1停用）")
    private String status;

    /** 用户是否存在此岗位标识 默认不存在 */
    @Schema(description = "用户是否存在此岗位标识 默认不存在")
    private boolean flag = false;
} 