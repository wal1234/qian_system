package com.qian.controller;

import com.qian.common.response.Response;
import com.qian.system.domain.SysConfig;
import com.qian.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController {

    @Autowired
    private ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @GetMapping("/list")
    public Response<List<SysConfig>> list(SysConfig config) {
        List<SysConfig> list = configService.selectConfigList(config);
        return Response.success(list);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @GetMapping("/{configId}")
    public Response<SysConfig> getInfo(@PathVariable Long configId) {
        return Response.success(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping("/configKey/{configKey}")
    public Response<String> getConfigKey(@PathVariable String configKey) {
        return Response.success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PostMapping
    public Response<Void> add(@RequestBody SysConfig config) {
        if ("1".equals(configService.checkConfigKeyUnique(config))) {
            return Response.error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return toResponse(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PutMapping
    public Response<Void> edit(@RequestBody SysConfig config) {
        if ("1".equals(configService.checkConfigKeyUnique(config))) {
            return Response.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return toResponse(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @DeleteMapping("/{configIds}")
    public Response<Void> remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return Response.success();
    }

    /**
     * 刷新参数缓存
     */
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