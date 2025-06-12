package com.qian.system.event;

import com.qian.system.domain.entity.SysConfig;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 配置变更事件
 */
@Getter
public class ConfigChangeEvent extends ApplicationEvent {
    private final SysConfig config;
    private final String operation; // 操作类型：ADD, UPDATE, DELETE

    public ConfigChangeEvent(Object source, SysConfig config, String operation) {
        super(source);
        this.config = config;
        this.operation = operation;
    }
} 