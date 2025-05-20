package com.qian.system.common.interceptor;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQL性能分析拦截器，用于输出每条SQL语句及其执行时间
 */
@Intercepts({
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class SqlCostInterceptor implements Interceptor {
    
    private static final Logger log = LoggerFactory.getLogger(SqlCostInterceptor.class);
    
    private long slowSqlMillis = 1000; // 默认慢SQL阈值为1秒
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql;
        
        // 获取SQL语句
        if (invocation.getArgs().length > 5) {
            boundSql = (BoundSql) invocation.getArgs()[5];
        } else {
            boundSql = mappedStatement.getBoundSql(parameter);
        }
        
        String sqlId = mappedStatement.getId();
        Configuration configuration = mappedStatement.getConfiguration();
        
        long start = System.currentTimeMillis();
        Object result = invocation.proceed();
        long end = System.currentTimeMillis();
        long time = end - start;
        
        if (time > slowSqlMillis) {
            String sql = getSql(configuration, boundSql, sqlId, time);
            log.warn("慢SQL > {} 执行耗时: {}ms", sql, time);
        } else if (log.isDebugEnabled()) {
            String sql = getSql(configuration, boundSql, sqlId, time);
            log.debug("SQL > {} 执行耗时: {}ms", sql, time);
        }
        
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String slowSqlMillisStr = properties.getProperty("slowSqlMillis");
        if (slowSqlMillisStr != null && !slowSqlMillisStr.isEmpty()) {
            try {
                this.slowSqlMillis = Long.parseLong(slowSqlMillisStr);
            } catch (NumberFormatException e) {
                log.warn("错误的slowSqlMillis值: {}, 使用默认值: {}ms", slowSqlMillisStr, this.slowSqlMillis);
            }
        }
    }
    
    /**
     * 格式化SQL语句
     */
    private String getSql(Configuration configuration, BoundSql boundSql, String sqlId, long time) {
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();
        
        // 替换参数值
        if (parameterMappings != null && !parameterMappings.isEmpty() && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else {
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        
        return sqlId + " - " + sql;
    }
    
    /**
     * 参数转换
     */
    private String getParameterValue(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof String) {
            return "'" + obj + "'";
        }
        if (obj instanceof Date) {
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            return "'" + dateFormat.format(obj) + "'";
        }
        return obj.toString();
    }
} 