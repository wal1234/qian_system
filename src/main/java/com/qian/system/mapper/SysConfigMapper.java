package com.qian.system.mapper;

import com.qian.system.domain.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 参数配置 数据层
 * 
 * 对应XML文件: mapper/system/SysConfigMapper.xml
 * SQL类型: MySQL
 * 
 * XML路径映射: classpath:mapper/system/SysConfigMapper.xml
 * MyBatis配置: mybatis-config.xml和mybatis-mapping-config.xml
 */
@Mapper
public interface SysConfigMapper {
    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     * @see mapper/system/SysConfigMapper.xml#selectConfigList
     */
    List<SysConfig> selectConfigList(SysConfig config);

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     * @see mapper/system/SysConfigMapper.xml#selectConfigById
     */
    @ResultMap("SysConfigResult")
    SysConfig selectConfigById(Long configId);

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数配置信息
     * @see mapper/system/SysConfigMapper.xml#selectConfigByKey
     */
    SysConfig selectConfigByKey(String configKey);

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    int insertConfig(SysConfig config);

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    int updateConfig(SysConfig config);

    /**
     * 删除参数配置
     *
     * @param configId 参数配置ID
     * @return 结果
     */
    int deleteConfigById(Long configId);

    /**
     * 批量删除参数配置
     *
     * @param configIds 需要删除的数据ID
     * @return 结果
     */
    int deleteConfigByIds(Long[] configIds);

    /**
     * 校验参数键名是否唯一
     *
     * @param configKey 参数键名
     * @return 结果
     */
    SysConfig checkConfigKeyUnique(String configKey);
} 