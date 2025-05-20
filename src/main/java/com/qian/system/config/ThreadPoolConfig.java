package com.qian.system.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {
    
    /** 核心线程池大小 */
    @Value("${thread-pool.core-pool-size:10}")
    private int corePoolSize;
    
    /** 最大可创建的线程数 */
    @Value("${thread-pool.max-pool-size:50}")
    private int maxPoolSize;
    
    /** 队列最大长度 */
    @Value("${thread-pool.queue-capacity:200}")
    private int queueCapacity;
    
    /** 线程池维护线程所允许的空闲时间 */
    @Value("${thread-pool.keep-alive-seconds:60}")
    private int keepAliveSeconds;
    
    /** 线程池前缀名 */
    @Value("${thread-pool.thread-name-prefix:qian-async-}")
    private String threadNamePrefix;

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);
        
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置等待时间（默认为0，此时立即停止）
        executor.setAwaitTerminationSeconds(60);
        // 初始化线程池
        executor.initialize();
        return executor;
    }
} 