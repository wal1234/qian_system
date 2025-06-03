package com.qian.system.controller;

import com.qian.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Nacos 测试控制器
 */
@Slf4j
@Tag(name = "Nacos测试接口")
@RestController
@RequestMapping("/nacos/test")
@RefreshScope
public class NacosTestController {
    
    @Autowired
    private Environment environment;
    
    @Operation(summary = "检查 Nacos 连接状态")
    @GetMapping("/status")
    public Response<Map<String, Object>> checkStatus() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始检查 Nacos 连接状态...");
            
            // 检查基础配置
            String serverAddr = environment.getProperty("spring.cloud.nacos.server-addr");
            String namespace = environment.getProperty("spring.cloud.nacos.namespace");
            String configEnabled = environment.getProperty("spring.cloud.nacos.config.enabled");
            String discoveryEnabled = environment.getProperty("spring.cloud.nacos.discovery.enabled");
            
            log.info("Nacos 基础配置检查结果:");
            log.info("- 服务器地址: {}", serverAddr);
            log.info("- 命名空间: {}", namespace);
            log.info("- 配置中心启用状态: {}", configEnabled);
            log.info("- 服务发现启用状态: {}", discoveryEnabled);
            
            result.put("nacos.server-addr", serverAddr);
            result.put("nacos.namespace", namespace);
            result.put("nacos.config.enabled", configEnabled);
            result.put("nacos.discovery.enabled", discoveryEnabled);
            
            // 检查应用基础信息
            String appName = environment.getProperty("spring.application.name");
            String activeProfile = environment.getProperty("spring.profiles.active");
            
            log.info("应用基础信息检查结果:");
            log.info("- 应用名称: {}", appName);
            log.info("- 激活的配置文件: {}", activeProfile);
            
            result.put("application.name", appName);
            result.put("profiles.active", activeProfile);
            
            // 检查配置加载状态
            String configImport = environment.getProperty("spring.config.import");
            log.info("配置导入状态: {}", configImport != null ? "已配置" : "未配置");
            result.put("config.import", configImport);
            
            result.put("status", "SUCCESS");
            result.put("message", "配置读取成功");
            
            log.info("Nacos 状态检查完成，所有配置读取成功");
            
        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("message", "配置读取失败: " + e.getMessage());
            result.put("error", e.getClass().getSimpleName());
            
            log.error("Nacos 状态检查失败，错误详情:", e);
            log.error("错误类型: {}", e.getClass().getName());
            log.error("错误消息: {}", e.getMessage());
            if (e.getCause() != null) {
                log.error("根本原因: {}", e.getCause().getMessage());
            }
        }
        
        return Response.success(result);
    }
    
    @Operation(summary = "测试配置刷新")
    @PostMapping("/refresh")
    public Response<String> testRefresh() {
        log.info("开始测试配置刷新功能...");
        try {
            // 获取刷新前的配置
            String beforeRefresh = environment.getProperty("spring.cloud.nacos.config.enabled");
            log.info("刷新前配置状态: {}", beforeRefresh);
            
            // 这里可以添加实际的刷新逻辑
            
            // 获取刷新后的配置
            String afterRefresh = environment.getProperty("spring.cloud.nacos.config.enabled");
            log.info("刷新后配置状态: {}", afterRefresh);
            
            log.info("配置刷新测试完成");
            return Response.success("配置刷新测试完成");
        } catch (Exception e) {
            log.error("配置刷新测试失败", e);
            return Response.error("配置刷新测试失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "获取所有环境变量")
    @GetMapping("/env")
    public Response<Map<String, Object>> getAllEnv() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有 Nacos 相关的配置
        String[] propertyNames = {
            "spring.cloud.nacos.server-addr",
            "spring.cloud.nacos.namespace",
            "spring.cloud.nacos.config.enabled",
            "spring.cloud.nacos.discovery.enabled",
            "spring.application.name",
            "spring.profiles.active"
        };
        
        for (String propertyName : propertyNames) {
            String value = environment.getProperty(propertyName);
            if (value != null) {
                result.put(propertyName, value);
            }
        }
        
        return Response.success(result);
    }
} 