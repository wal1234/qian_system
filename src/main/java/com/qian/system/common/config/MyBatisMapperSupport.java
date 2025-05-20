package com.qian.system.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * MyBatis Mapper支持类
 * 用于向IDE提供Mapper接口和XML文件的映射关系
 * 
 * 主要作用：
 * 1. 帮助IDE识别Mapper接口和XML文件的关联关系
 * 2. 支持在Mapper接口和XML文件之间跳转
 */
@Configuration
@ImportResource("classpath:mybatis-mapping-config.xml")
public class MyBatisMapperSupport {
    
    /**
     * 在此静态代码块中记录所有Mapper接口和XML文件的关联关系
     * 这段代码不会在运行时执行，仅用于IDE识别映射关系
     */
    static {
        // SysConfig映射
        put("com.qian.system.mapper.SysConfigMapper", "mapper/system/SysConfigMapper.xml");
        
        // SysMenu映射
        put("com.qian.system.mapper.SysMenuMapper", "mapper/system/SysMenuMapper.xml");
        
        // SysUser映射
        put("com.qian.system.mapper.SysUserMapper", "mapper/system/SysUserMapper.xml");
        
        // SysRole映射
        put("com.qian.system.mapper.SysRoleMapper", "mapper/system/SysRoleMapper.xml");
        
        // 其他Mapper映射...
    }
    
    /**
     * 辅助方法，用于记录映射关系
     * 此方法仅用于IDE识别映射关系，实际不会在运行时执行
     */
    private static void put(String mapperInterface, String xmlFile) {
        // 仅用于IDE识别映射关系，不会在运行时执行
    }
} 