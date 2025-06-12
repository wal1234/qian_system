package com.qian.system.controller;

import com.qian.common.annotation.Log;
import com.qian.common.response.Response;
import com.qian.common.core.page.PageDomain;
import com.qian.common.core.page.TableDataInfo;
import com.qian.common.enums.system.BusinessType;
import com.qian.common.utils.SecurityUtils;
import com.qian.common.utils.poi.ExcelUtil;
import com.qian.system.domain.entity.SysConfig;
import com.qian.system.service.ISysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 参数配置 信息操作处理
 */
@Tag(name = "参数配置管理")
@RestController
@RequestMapping("/system/config")
@RequiredArgsConstructor
public class SysConfigController {
    private final ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @Operation(summary = "获取参数配置列表")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public Response<List<SysConfig>> list(SysConfig config) {
        List<SysConfig> list = configService.selectConfigList(config);
        return Response.success(list);
    }

    /**
     * 导出参数配置列表
     */
    @Operation(summary = "导出参数配置列表")
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @Log(title = "参数管理", businessType = "EXPORT")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysConfig config) throws IOException {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<>(SysConfig.class);
        util.exportExcel(response, list, "参数数据");
    }

    /**
     * 根据参数编号获取详细信息
     */
    @Operation(summary = "根据参数编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping("/{configId}")
    public Response<SysConfig> getInfo(@PathVariable Long configId) {
        return Response.success(configService.getById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @Operation(summary = "根据参数键名查询参数值")
    @GetMapping("/configKey/{configKey}")
    public Response<String> getConfigKey(@PathVariable String configKey) {
        return Response.success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @Operation(summary = "新增参数配置")
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = "INSERT")
    @PostMapping
    public Response<Void> add(@Validated @RequestBody SysConfig config) {
        if (!configService.checkConfigKeyUnique(config)) {
            return Response.error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(SecurityUtils.getUsername());
        return Response.success();
    }

    /**
     * 修改参数配置
     */
    @Operation(summary = "修改参数配置")
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理", businessType = "UPDATE")
    @PutMapping
    public Response<Void> edit(@Validated @RequestBody SysConfig config) {
        if (!configService.checkConfigKeyUnique(config)) {
            return Response.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(SecurityUtils.getUsername());
        return Response.success();
    }

    /**
     * 删除参数配置
     */
    @Operation(summary = "删除参数配置")
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = "DELETE")
    @DeleteMapping("/{configIds}")
    public Response<Void> remove(@PathVariable Long[] configIds) {
        return Response.success();
    }

    /**
     * 刷新参数缓存
     */
    @Operation(summary = "刷新参数缓存")
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = "CLEAN")
    @DeleteMapping("/refreshCache")
    public Response<Void> refreshCache() {
        return Response.success();
    }
} 