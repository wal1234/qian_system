package com.qian.system.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MyBatis配置类
 * 
 * 注意：
 * 1. 本配置类与application.yml中的mybatis配置协同工作
 * 2. 类型别名包由application.yml的mybatis.typeAliasesPackage配置，这里不重复配置
 * 3. Mapper接口由@MapperScan注解扫描
 * 4. 每个Mapper接口也应添加@Mapper注解，确保它们能够被正确识别
 * 5. @MapperScan与各接口上的@Mapper注解共同作用，不会冲突
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.qian.system.mapper"})
public class MybatisConfig {
    
    private static final Logger log = LoggerFactory.getLogger(MybatisConfig.class);
    
    @Value("${mybatis.typeAliasesPackage:com.qian.system.domain.**}")
    private String typeAliasesPackage;
    
    @Value("${mybatis.mapperLocations:classpath*:mapper/**/*.xml}")
    private String mapperLocations;
    
    /**
     * 配置SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        long startTime = System.currentTimeMillis();
        log.info("开始初始化MyBatis SqlSessionFactory");
        
        try {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        
        // 设置MyBatis配置
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(true);
        configuration.setLocalCacheScope(LocalCacheScope.SESSION);
        configuration.setUseGeneratedKeys(true);
        configuration.setDefaultExecutorType(ExecutorType.REUSE);
        configuration.setDefaultStatementTimeout(30);
        configuration.setDefaultFetchSize(100);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCallSettersOnNulls(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setLazyLoadingEnabled(true);
        configuration.setAggressiveLazyLoading(false);
        configuration.setMultipleResultSetsEnabled(true);
        configuration.setLogImpl(org.apache.ibatis.logging.slf4j.Slf4jImpl.class);
        
        sessionFactory.setConfiguration(configuration);
            
            // 全面兼容各种包路径配置，确保所有实体类都能被正确扫描
            String finalTypeAliasesPackage = typeAliasesPackage;
            // 移除重复的包路径配置，只使用system包下的实体类
            if (finalTypeAliasesPackage.contains("com.qian.common.core.domain")) {
                log.info("检测到已包含com.qian.common.core.domain包，将使用system包下的实体类");
                finalTypeAliasesPackage = "com.qian.system.domain";
            }
            
            log.info("设置类型别名包: {}", finalTypeAliasesPackage);
            sessionFactory.setTypeAliasesPackage(finalTypeAliasesPackage);
        
        // 设置mapper的xml文件路径
            Resource[] mapperResources = resolveAllMapperLocations();
            
            // 验证每个Mapper XML文件
            List<Resource> validResources = new ArrayList<>();
            for (Resource resource : mapperResources) {
                try {
                    if (validateMapperXml(resource)) {
                        validResources.add(resource);
                        log.info("验证通过: {}", resource.getFilename());
                    } else {
                        log.warn("Mapper XML文件验证失败，跳过: {}", resource.getFilename());
                    }
                } catch (Exception e) {
                    log.warn("验证Mapper XML时出错，跳过: {} - {}", resource.getFilename(), e.getMessage());
                }
            }
            
            Resource[] validMapperResources = validResources.toArray(new Resource[0]);
            sessionFactory.setMapperLocations(validMapperResources);
            log.info("加载了{}个有效的Mapper XML文件", validMapperResources.length);
        
        // 创建SqlSessionFactory
        SqlSessionFactory factory = sessionFactory.getObject();
        if (factory == null) {
            throw new IllegalStateException("创建SqlSessionFactory失败");
        }
        
        long costTime = System.currentTimeMillis() - startTime;
        log.info("MyBatis SqlSessionFactory初始化完成，耗时: {}ms", costTime);
        return factory;
        } catch (Exception e) {
            log.error("初始化MyBatis SqlSessionFactory时发生错误: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 验证Mapper XML文件
     */
    private boolean validateMapperXml(Resource resource) {
        if (!resource.exists()) {
            log.warn("Mapper资源不存在: {}", resource.getFilename());
            return false;
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            // 检查XML基本结构
            String line;
            boolean hasDocType = false;
            boolean hasMapper = false;
            boolean hasNamespace = false;
            
            while ((line = reader.readLine()) != null) {
                if (line.contains("DOCTYPE mapper")) {
                    hasDocType = true;
                }
                if (line.contains("<mapper")) {
                    hasMapper = true;
                }
                if (line.contains("namespace=")) {
                    hasNamespace = true;
                    // 检查命名空间是否指向有效的Mapper接口
                    String namespace = line.replaceAll(".*namespace=\"([^\"]+)\".*", "$1");
                    log.debug("检测到Mapper命名空间: {}", namespace);
                }
            }
            
            if (!hasDocType || !hasMapper || !hasNamespace) {
                log.warn("Mapper XML文件格式不正确: {}. DOCTYPE: {}, <mapper>: {}, namespace: {}", 
                    resource.getFilename(), hasDocType, hasMapper, hasNamespace);
                return false;
            }
            
            return true;
        } catch (Exception e) {
            log.warn("读取Mapper XML文件时出错: {} - {}", resource.getFilename(), e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取所有mapper的xml文件资源，避免重复加载
     */
    private Resource[] resolveAllMapperLocations() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Set<String> uniqueResourcePaths = new HashSet<>();
        List<Resource> resourceList = new ArrayList<>();
        
        try {
            // 只使用一个固定的Mapper XML文件路径
            String locationPattern = "classpath:mapper/**/*.xml";
            log.info("解析Mapper位置: {}", locationPattern);
            
            Resource[] resources = resolver.getResources(locationPattern.trim());
            log.info("从位置 {} 加载了 {} 个Mapper XML文件", locationPattern.trim(), resources.length);
            addResourcesToList(resources, resourceList, uniqueResourcePaths);
            
            if (resourceList.isEmpty()) {
                log.warn("未找到任何MyBatis Mapper XML文件");
            } else {
                log.info("成功加载 {} 个唯一的Mapper XML文件", resourceList.size());
                if (log.isDebugEnabled()) {
                    for (Resource resource : resourceList) {
                        log.debug("已加载Mapper XML: {}", resource.getURL());
                    }
                }
            }
        } catch (IOException e) {
            log.error("加载Mapper XML资源失败: {}", e.getMessage(), e);
            throw e;
        }
        
        return resourceList.toArray(new Resource[0]);
    }
    
    /**
     * 将资源添加到列表，避免重复
     */
    private void addResourcesToList(Resource[] resources, List<Resource> targetList, Set<String> uniquePaths) {
        if (resources == null || resources.length == 0) {
            return;
        }
        
        for (Resource resource : resources) {
            try {
                if (resource.exists()) {
                String path = resource.getURL().toString();
                if (uniquePaths.add(path)) {
                    targetList.add(resource);
                        log.debug("添加Mapper XML: {}", path);
                    } else {
                        log.debug("跳过重复的Mapper XML: {}", path);
                    }
                } else {
                    log.warn("Mapper资源不存在: {}", resource);
                }
            } catch (IOException e) {
                log.warn("获取资源URL失败: {}", resource, e);
            }
        }
    }
} 