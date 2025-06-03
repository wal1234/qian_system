package com.qian.system.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 配置变更事件监听器
 */
@Slf4j
@Component
public class ConfigChangeEventListener {

    /**
     * 异步处理配置变更事件
     */
    @Async
    @EventListener
    public void handleConfigChangeEvent(ConfigChangeEvent event) {
        try {
            log.info("收到配置变更事件：configKey={}, operation={}", 
                event.getConfig().getConfigKey(), event.getOperation());
            
            // TODO: 在这里添加配置变更后的业务处理逻辑
            // 例如：
            // 1. 发送配置变更通知
            // 2. 更新相关业务缓存
            // 3. 触发相关业务逻辑
            
        } catch (Exception e) {
            log.error("处理配置变更事件时发生错误", e);
        }
    }
} 