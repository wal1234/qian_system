package com.qian.system.domain.entity;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 树形结构实体类
 */
@Data
@Schema(description = "树形结构实体类")
public class TreeSelect {
    /** 节点ID */
    @NotNull(message = "节点ID不能为空")
    @Schema(description = "节点ID")
    private Long id;

    /** 节点名称 */
    @NotBlank(message = "节点名称不能为空")
    @Schema(description = "节点名称")
    private String label;

    /** 子节点 */
    @Schema(description = "子节点")
    private List<TreeSelect> children;

    public TreeSelect() {
    }

    public TreeSelect(Long id, String label) {
        this.id = id;
        this.label = label;
    }
} 