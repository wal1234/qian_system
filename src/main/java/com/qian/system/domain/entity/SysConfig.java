package com.qian.system.domain.entity;

import java.io.Serializable;
import com.qian.common.annotation.Excel;
import com.qian.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

/**
 * 参数配置表 sys_config
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfig extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 参数主键 */
    @Excel(name = "参数主键", cellType = Excel.ColumnType.NUMERIC)
    private Long configId;

    /** 参数名称 */
    @Excel(name = "参数名称")
    @NotBlank(message = "参数名称不能为空")
    @Size(min = 0, max = 100, message = "参数名称不能超过100个字符")
    private String configName;

    /** 参数键名 */
    @Excel(name = "参数键名")
    @NotBlank(message = "参数键名不能为空")
    @Size(min = 0, max = 100, message = "参数键名长度不能超过100个字符")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "参数键名只能包含字母、数字和下划线，且必须以字母开头")
    private String configKey;

    /** 参数键值 */
    @Excel(name = "参数键值")
    @NotBlank(message = "参数键值不能为空")
    @Size(min = 0, max = 500, message = "参数键值长度不能超过500个字符")
    private String configValue;

    /** 系统内置（Y是 N否） */
    @Excel(name = "系统内置", readConverterExp = "Y=是,N=否")
    @Pattern(regexp = "^[YN]$", message = "系统内置只能是Y或N")
    private String configType;

    /** 备注 */
    @Excel(name = "备注")
    @Size(min = 0, max = 500, message = "备注长度不能超过500个字符")
    private String remark;
} 