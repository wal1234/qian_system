package com.qian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动程序
 */
@SpringBootApplication
@EnableDiscoveryClient
public class QianSystemApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        // 设置Tomcat临时目录 
        System.setProperty("java.io.tmpdir", System.getProperty("user.dir") + "/temp");
        // 解决Windows下性能问题
        System.setProperty("spring.jmx.enabled", "false");
        // 禁用Eureka自动配置
        System.setProperty("eureka.client.enabled", "false");
        System.setProperty("eureka.instance.enabled", "false");
        SpringApplication.run(QianSystemApplication.class, args);
    }
} 