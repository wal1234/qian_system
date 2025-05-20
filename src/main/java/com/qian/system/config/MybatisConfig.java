package com.qian.system.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.core.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
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
    
    /**
     * 配置SqlSessionFactory
     * 与application.yml中的mybatis配置协同工作
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        long startTime = System.currentTimeMillis();
        log.info("开始初始化MyBatis SqlSessionFactory");
        
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        
        // 设置MyBatis配置文件路径
        Resource configLocation = new PathMatchingResourcePatternResolver()
                .getResource("classpath:mybatis/mybatis-config.xml");
        sessionFactory.setConfigLocation(configLocation);
        log.info("加载MyBatis配置文件: {}", configLocation.getURL());
        
        // 注意：不在这里设置typeAliasesPackage，完全依靠application.yml中的配置
        // 这样避免与application.yml中的mybatis.typeAliasesPackage配置冲突
        
        // 设置mapper的xml文件路径 - 合并多种方式找到的XML资源
        Resource[] mapperLocations = resolveAllMapperLocations();
        sessionFactory.setMapperLocations(mapperLocations);
        log.info("加载了{}个Mapper XML文件", mapperLocations.length);
        
        // 创建SqlSessionFactory
        SqlSessionFactory factory = sessionFactory.getObject();
        if (factory == null) {
            throw new IllegalStateException("创建SqlSessionFactory失败");
        }
        
        long costTime = System.currentTimeMillis() - startTime;
        log.info("MyBatis SqlSessionFactory初始化完成，耗时: {}ms", costTime);
        return factory;
    }
    
    /**
     * 获取所有mapper的xml文件资源，避免重复加载
     */
    private Resource[] resolveAllMapperLocations() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Set<String> uniqueResourcePaths = new HashSet<>();
        List<Resource> resourceList = new ArrayList<>();
        
        try {
            // 1. 首先加载标准路径
            Resource[] resources = resolver.getResources("classpath:mapper/**/*.xml");
            addResourcesToList(resources, resourceList, uniqueResourcePaths);
            
            // 2. 添加系统模块路径
            resources = resolver.getResources("classpath*:mapper/system/*.xml");
            addResourcesToList(resources, resourceList, uniqueResourcePaths);
            
            // 3. 确保关键Mapper被加载
            addSpecificMapperIfNeeded("mapper/system/SysConfigMapper.xml", resourceList, uniqueResourcePaths);
            addSpecificMapperIfNeeded("mapper/system/SysUserMapper.xml", resourceList, uniqueResourcePaths);
            addSpecificMapperIfNeeded("mapper/system/SysRoleMapper.xml", resourceList, uniqueResourcePaths);
            addSpecificMapperIfNeeded("mapper/system/SysMenuMapper.xml", resourceList, uniqueResourcePaths);
            
        } catch (IOException e) {
            log.error("加载Mapper XML资源失败", e);
            throw e;
        }
        
        if (resourceList.isEmpty()) {
            log.warn("未找到任何MyBatis Mapper XML文件");
        } else {
            if (log.isDebugEnabled()) {
                for (Resource resource : resourceList) {
                    log.debug("已加载Mapper XML: {}", resource.getURL());
                }
            }
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
                String path = resource.getURL().toString();
                if (uniquePaths.add(path)) {
                    targetList.add(resource);
                }
            } catch (IOException e) {
                log.warn("获取资源URL失败: {}", resource, e);
            }
        }
    }
    
    /**
     * 添加特定的Mapper文件，如果尚未添加
     */
    private void addSpecificMapperIfNeeded(String mapperPath, List<Resource> targetList, Set<String> uniquePaths) {
        try {
            ClassPathResource resource = new ClassPathResource(mapperPath);
            if (resource.exists()) {
                String path = resource.getURL().toString();
                if (uniquePaths.add(path)) {
                    targetList.add(resource);
                    log.info("显式加载Mapper: {}", mapperPath);
                }
            }
        } catch (IOException e) {
            log.warn("加载特定Mapper失败: {}", mapperPath, e);
        }
    }
} 