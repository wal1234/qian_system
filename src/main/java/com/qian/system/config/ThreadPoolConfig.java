package com.qian.system.config;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import jakarta.annotation.PreDestroy;

/**
 * 线程池配置
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {
    
    private static final Logger log = LoggerFactory.getLogger(ThreadPoolConfig.class);
    
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
    
    /** 拒绝策略类型：ABORT(默认), CALLER_RUNS, DISCARD, DISCARD_OLDEST */
    @Value("${thread-pool.rejection-policy:CALLER_RUNS}")
    private String rejectionPolicy;
    
    /** 系统关闭等待线程池结束的最大时间（秒） */
    @Value("${thread-pool.shutdown-timeout:15}")
    private int shutdownTimeout;
    
    private ThreadPoolTaskExecutor executor;
    
    /**
     * 获取异步任务执行器
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);
        
        // 配置拒绝策略
        executor.setRejectedExecutionHandler(getRejectedExecutionHandler());
        
        // 线程池初始化
        executor.initialize();
        log.info("线程池初始化完成 - 核心线程数:{}, 最大线程数:{}, 队列容量:{}, 空闲时间:{}秒, 拒绝策略:{}",
                corePoolSize, maxPoolSize, queueCapacity, keepAliveSeconds, rejectionPolicy);
        
        return executor;
    }
    
    /**
     * 根据配置获取拒绝策略
     */
    private RejectedExecutionHandler getRejectedExecutionHandler() {
        switch (rejectionPolicy.toUpperCase()) {
            case "CALLER_RUNS":
                return new ThreadPoolExecutor.CallerRunsPolicy();
            case "DISCARD":
                return new ThreadPoolExecutor.DiscardPolicy();
            case "DISCARD_OLDEST":
                return new ThreadPoolExecutor.DiscardOldestPolicy();
            case "ABORT":
            default:
                return new ThreadPoolExecutor.AbortPolicy();
        }
    }
    
    /**
     * 应用关闭前优雅关闭线程池
     */
    @PreDestroy
    public void shutdown() {
        if (executor != null) {
            log.info("关闭线程池，等待最多{}秒完成任务...", shutdownTimeout);
            executor.setWaitForTasksToCompleteOnShutdown(true);
            executor.setAwaitTerminationSeconds(shutdownTimeout);
            
            // 监控关闭过程
            Thread monitor = new Thread(() -> {
                long start = System.currentTimeMillis();
                while (!executor.getThreadPoolExecutor().isTerminated()) {
                    try {
                        // 每秒打印一次线程池状态
                        int activeCount = executor.getActiveCount();
                        long completedTasks = executor.getThreadPoolExecutor().getCompletedTaskCount();
                        long totalTasks = executor.getThreadPoolExecutor().getTaskCount();
                        log.info("线程池关闭中 - 活动线程:{}, 已完成任务:{}, 总任务:{}", activeCount, completedTasks, totalTasks);
                        
                        TimeUnit.SECONDS.sleep(1);
                        
                        // 超过最大等待时间则强制打印警告
                        if (System.currentTimeMillis() - start > shutdownTimeout * 1000L) {
                            log.warn("线程池关闭超时，可能存在未完成的任务");
                            break;
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                log.info("线程池已关闭");
            });
            
            monitor.setDaemon(true);
            monitor.setName("thread-pool-shutdown-monitor");
            monitor.start();
        }
    }
} 