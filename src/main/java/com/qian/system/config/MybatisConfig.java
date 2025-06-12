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

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            
            // 设置类型别名包
            log.info("设置类型别名包: {}", typeAliasesPackage);
            sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
            
            // 设置mapper的xml文件路径
            Resource[] mapperResources = resolveMapperLocations();
            sessionFactory.setMapperLocations(mapperResources);
            log.info("加载了{}个Mapper XML文件", mapperResources.length);
            
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
     * 获取所有mapper的xml文件资源
     */
    private Resource[] resolveMapperLocations() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();
        
        try {
            Resource[] mapperResources = resolver.getResources(mapperLocations);
            for (Resource resource : mapperResources) {
                if (resource.exists()) {
                    resources.add(resource);
                    log.debug("加载Mapper XML: {}", resource.getFilename());
                } else {
                    log.warn("Mapper资源不存在: {}", resource.getFilename());
                }
            }
            
            if (resources.isEmpty()) {
                log.warn("未找到任何MyBatis Mapper XML文件");
            } else {
                log.info("成功加载 {} 个Mapper XML文件", resources.size());
            }
        } catch (IOException e) {
            log.error("加载Mapper XML资源失败: {}", e.getMessage(), e);
            throw e;
        }
        
        return resources.toArray(new Resource[0]);
    }
} 