package com.qian.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 参数配置表 sys_config
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "参数配置信息")
public class SysConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 参数主键 */
    @Schema(description = "参数主键")
    private Long configId;

    /** 参数名称 */
    @NotBlank(message = "参数名称不能为空")
    @Size(min = 0, max = 100, message = "参数名称不能超过100个字符")
    @Schema(description = "参数名称", required = true)
    private String configName;

    /** 参数键名 */
    @NotBlank(message = "参数键名不能为空")
    @Size(min = 0, max = 100, message = "参数键名不能超过100个字符")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "参数键名必须以字母开头，只能包含字母、数字和下划线")
    @Schema(description = "参数键名", required = true)
    private String configKey;

    /** 参数键值 */
    @NotBlank(message = "参数键值不能为空")
    @Size(min = 0, max = 500, message = "参数键值不能超过500个字符")
    @Schema(description = "参数键值", required = true)
    private String configValue;

    /** 系统内置（Y是 N否） */
    @Pattern(regexp = "^[YN]$", message = "系统内置只能是Y或N")
    @Schema(description = "系统内置（Y是 N否）")
    private String configType;
} 