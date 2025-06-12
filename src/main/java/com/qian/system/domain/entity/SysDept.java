package com.qian.system.domain.entity;

import java.util.ArrayList;
import java.util.List;
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
 * 部门表 sys_dept
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
@Schema(description = "部门信息")
public class SysDept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 部门ID */
    @TableId(value = "dept_id", type = IdType.AUTO)
    @Excel(name = "部门编号", cellType = ColumnType.NUMERIC)
    @Schema(description = "部门ID")
    private Long deptId;

    /** 父部门ID */
    @Excel(name = "父部门编号", cellType = ColumnType.NUMERIC)
    @Schema(description = "父部门ID")
    private Long parentId;

    /** 祖级列表 */
    @Schema(description = "祖级列表")
    private String ancestors;

    /** 部门名称 */
    @Excel(name = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    @Schema(description = "部门名称")
    private String deptName;

    /** 显示顺序 */
    @Excel(name = "显示顺序", cellType = ColumnType.NUMERIC)
    @NotNull(message = "显示顺序不能为空")
    @Schema(description = "显示顺序")
    private Integer orderNum;

    /** 负责人 */
    @Excel(name = "负责人")
    @Schema(description = "负责人")
    private String leader;

    /** 联系电话 */
    @Excel(name = "联系电话")
    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    @Schema(description = "联系电话")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    @Schema(description = "邮箱")
    private String email;

    /** 部门状态（0正常 1停用） */
    @Excel(name = "部门状态", readConverterExp = "0=正常,1=停用")
    @Pattern(regexp = "^[01]$", message = "部门状态只能是0或1")
    @Schema(description = "部门状态（0正常 1停用）")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    @Pattern(regexp = "^[02]$", message = "删除标志只能是0或2")
    @Schema(description = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /** 父部门名称 */
    @TableField(exist = false)
    @Schema(description = "父部门名称")
    private String parentName;

    /** 子部门 */
    @TableField(exist = false)
    @Schema(description = "子部门")
    private List<SysDept> children = new ArrayList<>();

    public SysDept() {
    }

    public SysDept(Long deptId) {
        this.deptId = deptId;
    }
} 