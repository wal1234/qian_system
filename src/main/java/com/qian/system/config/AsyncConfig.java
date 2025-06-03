package com.qian.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 异步配置
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    // 可以在这里配置异步线程池等
} 