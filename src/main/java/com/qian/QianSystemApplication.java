package com.qian;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.env.Environment;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 启动程序
 */
@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(
    info = @Info(
        title = "千博客系统接口文档",
        version = "1.0.0",
        description = "千博客系统接口文档"
    )
)
@ComponentScan(
    basePackages = {"com.qian.system", "com.qian.common"},
    excludeFilters = {
        @Filter(type = FilterType.REGEX, pattern = "com.qian.common.feign.FeignConfig"),
        @Filter(type = FilterType.REGEX, pattern = ".*GlobalExceptionHandler")
    }
)
public class QianSystemApplication extends SpringBootServletInitializer {
    
    private static final Logger log = LoggerFactory.getLogger(QianSystemApplication.class);
    
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        
        // 设置系统属性
        setSystemProperties();
        
        // 启动应用
        ConfigurableApplicationContext context = SpringApplication.run(QianSystemApplication.class, args);
        
        // 输出启动信息
        printStartupInfo(context, startTime);
    }
    
    /**
     * 设置系统属性
     */
    private static void setSystemProperties() {
        // 设置Tomcat临时目录 
        System.setProperty("java.io.tmpdir", System.getProperty("user.dir") + "/temp");
        // 解决Windows下性能问题
        System.setProperty("spring.jmx.enabled", "false");
        // 优化垃圾回收
        System.setProperty("file.encoding", "UTF-8");
    }
    
    /**
     * 输出应用启动信息
     */
    private static void printStartupInfo(ConfigurableApplicationContext context, long startTime) {
        Environment env = context.getEnvironment();
        String ip = "localhost";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("获取本机IP地址失败", e);
        }
        
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String activeProfile = env.getProperty("spring.profiles.active", "default");
        
        log.info("\n----------------------------------------------------------\n" +
                "应用 '{}' 启动成功! 访问URL:\n" +
                "本地: \t\thttp://localhost:{}{}\n" +
                "外部: \t\thttp://{}:{}{}\n" +
                "环境: \t\t{}\n" +
                "启动耗时: \t{}ms\n" +
                "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                port, contextPath,
                ip, port, contextPath,
                activeProfile,
                System.currentTimeMillis() - startTime);
    }
} 