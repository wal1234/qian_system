package com.qian.system.config;

import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import java.util.concurrent.Executor;

@Slf4j
@Configuration
@RefreshScope
public class NacosConfig {
    
    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh(RefreshScopeRefreshedEvent event) {
        log.info("Nacos config refreshed successfully");
    }
    
    @Bean
    public Listener nacosConfigListener() {
        return new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("Received Nacos config update: {}", configInfo);
            }
            
            @Override
            public Executor getExecutor() {
                return null;
            }
        };
    }
} 