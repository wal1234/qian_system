package com.qian.system.common.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.qian.system.config.MybatisConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 * 高级MyBatis扫描器配置
 * 
 * 该配置类默认被禁用，避免与MybatisConfig中的@MapperScan注解重复扫描
 * 只有明确设置配置项 mybatis.use-custom-scanner=true 时才会启用
 * 
 * 使用场景：
 * 1. 需要更复杂的Mapper扫描规则
 * 2. 需要自定义名称生成规则
 * 3. 需要特殊处理某些Mapper接口
 */
@Configuration
@AutoConfigureAfter(MybatisConfig.class)
@ConditionalOnProperty(name = "mybatis.use-custom-scanner", havingValue = "true", matchIfMissing = false)
public class MyBatisScannerConfig {
    
    private static final Logger log = LoggerFactory.getLogger(MyBatisScannerConfig.class);
    
    /**
     * 配置扫描Mapper接口的扫描器
     * 注意：此扫描器将替代MybatisConfig中的@MapperScan配置
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        log.info("启用自定义MyBatis Mapper扫描器");
        
        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
        // 设置扫描的基础包，要与MybatisConfig中的保持一致
        scannerConfigurer.setBasePackage("com.qian.system.mapper");
        // 设置SQLSessionFactory的bean名称
        scannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        // 设置名称生成器，确保接口和XML文件名称一致
        scannerConfigurer.setNameGenerator((beanDefinition, registry) -> {
            String className = beanDefinition.getBeanClassName();
            if (className != null && className.endsWith("Mapper")) {
                return className.substring(className.lastIndexOf(".") + 1);
            }
            return className;
        });
        
        log.info("自定义MyBatis Mapper扫描器配置完成，将扫描包: com.qian.system.mapper");
        return scannerConfigurer;
    }
    
    /**
     * 辅助方法：获取所有Mapper的XML文件路径
     * 注意：此方法仅用于调试目的，不会被Spring容器使用
     */
    private Resource[] getMapperXmlResources() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:mapper/**/*Mapper.xml");
        if (log.isDebugEnabled() && resources != null) {
            log.debug("找到{}个Mapper XML文件", resources.length);
        }
        return resources;
    }
} 