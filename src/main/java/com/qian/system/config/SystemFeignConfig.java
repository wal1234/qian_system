package com.qian.system.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * 系统模块Feign配置类
 */
@Configuration
public class SystemFeignConfig {
    
    /**
     * 日志级别配置
     */
    @Bean
    @ConditionalOnMissingBean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    
    /**
     * 错误解码器配置
     */
    @Bean
    @ConditionalOnMissingBean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default();
    }
} 