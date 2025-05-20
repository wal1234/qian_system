package com.qian.system.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tomcat服务器配置
 */
@Configuration
public class TomcatConfig {

    /**
     * 自定义Tomcat配置，解决启动问题
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        // 配置协议版本
        factory.setProtocol("HTTP/1.1");
        // 设置为最大100个发送缓冲池
        factory.addConnectorCustomizers(connector -> {
            connector.setProperty("maxKeepAliveRequests", "100");
            connector.setProperty("relaxedPathChars", "[]|{}^\\`%");
            connector.setProperty("relaxedQueryChars", "[]|{}^\\`%");
        });
        return factory;
    }
} 