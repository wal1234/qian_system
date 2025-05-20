package com.qian.system.common.interceptor;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * 支持慢SQL日志记录与敏感信息过滤
 */
@Intercepts({
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class SqlCostInterceptor implements Interceptor {
    
    private static final Logger log = LoggerFactory.getLogger(SqlCostInterceptor.class);
    private static final Pattern SENSITIVE_PATTERN = Pattern.compile(
        "(password|pwd|passwd|secret|token|key)\\s*[:=]\\s*['\"]([^'\"]+)['\"]", 
        Pattern.CASE_INSENSITIVE
    );
    
    // 默认慢SQL阈值为1秒
    private long slowSqlMillis = 1000;
    // 最大SQL长度，超过则截断
    private int maxSqlLength = 2000;
    // 是否启用敏感信息过滤
    private boolean enableSensitiveFilter = true;
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
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
            
            // 记录SQL执行时间
            long start = System.currentTimeMillis();
            Object result;
            try {
                result = invocation.proceed();
            } catch (Exception e) {
                // 记录SQL执行异常，确保不暴露敏感信息
                String sql = getSql(configuration, boundSql, sqlId, -1);
                log.error("SQL执行异常 > {}", sql, e);
                throw e;
            }
            long end = System.currentTimeMillis();
            long time = end - start;
            
            // 根据执行时间记录日志
            if (time > slowSqlMillis) {
                String sql = getSql(configuration, boundSql, sqlId, time);
                log.warn("慢SQL > {} 执行耗时: {}ms", sql, time);
            } else if (log.isDebugEnabled()) {
                String sql = getSql(configuration, boundSql, sqlId, time);
                log.debug("SQL > {} 执行耗时: {}ms", sql, time);
            }
            
            return result;
        } catch (Exception e) {
            // 拦截器异常不应影响正常业务执行
            log.error("SQL拦截器异常", e);
            return invocation.proceed();
        }
    }

    @Override
    public Object plugin(Object target) {
        // 只拦截Executor对象
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        // 设置慢SQL阈值
        String slowSqlMillisStr = properties.getProperty("slowSqlMillis");
        if (slowSqlMillisStr != null && !slowSqlMillisStr.isEmpty()) {
            try {
                this.slowSqlMillis = Long.parseLong(slowSqlMillisStr);
            } catch (NumberFormatException e) {
                log.warn("错误的slowSqlMillis值: {}, 使用默认值: {}ms", slowSqlMillisStr, this.slowSqlMillis);
            }
        }
        
        // 设置最大SQL长度
        String maxSqlLengthStr = properties.getProperty("maxSqlLength");
        if (maxSqlLengthStr != null && !maxSqlLengthStr.isEmpty()) {
            try {
                this.maxSqlLength = Integer.parseInt(maxSqlLengthStr);
            } catch (NumberFormatException e) {
                log.warn("错误的maxSqlLength值: {}, 使用默认值: {}", maxSqlLengthStr, this.maxSqlLength);
            }
        }
        
        // 设置是否启用敏感信息过滤
        String enableSensitiveFilterStr = properties.getProperty("enableSensitiveFilter");
        if (enableSensitiveFilterStr != null && !enableSensitiveFilterStr.isEmpty()) {
            this.enableSensitiveFilter = Boolean.parseBoolean(enableSensitiveFilterStr);
        }
        
        // 记录配置信息
        log.info("SQL拦截器配置 - 慢SQL阈值: {}ms, 最大SQL长度: {}, 敏感信息过滤: {}", 
                this.slowSqlMillis, this.maxSqlLength, this.enableSensitiveFilter);
    }
    
    /**
     * 格式化SQL语句
     */
    private String getSql(Configuration configuration, BoundSql boundSql, String sqlId, long time) {
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();
        
        try {
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
        } catch (Exception e) {
            // 参数解析失败时，使用原始SQL，避免完全无法记录
            log.warn("SQL参数解析异常: {}", e.getMessage());
        }
        
        // 过滤敏感信息
        if (enableSensitiveFilter) {
            sql = filterSensitiveInfo(sql);
        }
        
        // 截断过长的SQL
        if (sql.length() > maxSqlLength) {
            sql = sql.substring(0, maxSqlLength) + "... [截断]";
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
            String text = (String) obj;
            // 过滤敏感参数和超长文本
            if (isSensitiveParameter(text)) {
                return "'******'";
            }
            if (text.length() > 100) {
                return "'" + text.substring(0, 97) + "...'";
            }
            return "'" + text + "'";
        }
        if (obj instanceof Date) {
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            return "'" + dateFormat.format(obj) + "'";
        }
        return obj.toString();
    }
    
    /**
     * 判断是否是敏感参数
     */
    private boolean isSensitiveParameter(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        String lowerCase = value.toLowerCase();
        return lowerCase.contains("password") || 
               lowerCase.contains("pwd") ||
               lowerCase.contains("secret") ||
               lowerCase.contains("token") ||
               lowerCase.contains("key");
    }
    
    /**
     * 过滤SQL中的敏感信息
     */
    private String filterSensitiveInfo(String sql) {
        if (sql == null || sql.isEmpty()) {
            return sql;
        }
        
        Matcher matcher = SENSITIVE_PATTERN.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1) + " = '******'");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
} 