package com.qian.system.controller;

import com.qian.common.annotation.Log;
import com.qian.common.enums.system.BusinessType;
import com.qian.common.response.Response;
import com.qian.system.common.core.controller.BaseController;
import com.qian.system.domain.SysConfig;
import com.qian.system.service.ISysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 */
@Tag(name = "参数配置管理")
@RestController
@Slf4j
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {
    @Autowired
    private ISysConfigService configService;

    @Operation(summary = "获取参数配置列表")
    //@PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public Response<List<SysConfig>> list(SysConfig config) {
        log.info("获取参数配置列表，参数：{}", config);
        List<SysConfig> list = configService.selectConfigList(config);
        log.info("查询到配置数量：{}", list != null ? list.size() : 0);
        return Response.success(list);
    }

    @Operation(summary = "根据参数编号获取详细信息")
    //@PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping("/{configId}")
    public Response<SysConfig> getInfo(@Parameter(description = "参数ID") @PathVariable Long configId) {
        log.info("根据参数编号获取详细信息{}", configId);
        return Response.success(configService.selectConfigById(configId));
    }

    @Operation(summary = "根据参数键名查询参数值")
    @GetMapping("/configKey/{configKey}")
    public Response<String> getConfigKey(@Parameter(description = "参数键名") @PathVariable String configKey) {
        return Response.success(configService.selectConfigByKey(configKey));
    }

    @Operation(summary = "新增参数配置")
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Response<Void> add(@Validated @RequestBody SysConfig config) {
        if ("1".equals(configService.checkConfigKeyUnique(config))) {
            return Response.error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(getUsername());
        return toResponse(configService.insertConfig(config));
    }

    @Operation(summary = "修改参数配置")
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Response<Void> edit(@Validated @RequestBody SysConfig config) {
        if ("1".equals(configService.checkConfigKeyUnique(config))) {
            return Response.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(getUsername());
        return toResponse(configService.updateConfig(config));
    }

    @Operation(summary = "删除参数配置")
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public Response<Void> remove(@Parameter(description = "参数ID数组") @PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return Response.success();
    }

    @Operation(summary = "刷新参数缓存")
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public Response<Void> refreshCache() {
        configService.resetConfigCache();
        return Response.success();
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    private Response<Void> toResponse(int rows) {
        return rows > 0 ? Response.success() : Response.error("操作失败");
    }
} 