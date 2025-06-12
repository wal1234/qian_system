package com.qian.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qian.system.domain.entity.SysConfig;

import java.util.List;

/**
 * 参数配置 服务层
 */
public interface ISysConfigService extends IService<SysConfig> {
    /**
     * 查询参数配置列表
     * 
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    public List<SysConfig> selectConfigList(SysConfig config);

    /**
     * 查询参数配置信息
     * 
     * @param config 参数配置信息
     * @return 参数配置信息
     */
    public SysConfig selectConfig(SysConfig config);

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey 参数键名
     * @return 参数键值
     */
    public String selectConfigByKey(String configKey);

    /**
     * 校验参数键名是否唯一
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    public boolean checkConfigKeyUnique(SysConfig config);

    /**
     * 新增参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    public boolean insertConfig(SysConfig config);

    /**
     * 修改参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    public boolean updateConfig(SysConfig config);

    /**
     * 删除参数配置信息
     * 
     * @param configId 参数配置ID
     * @return 结果
     */
    public boolean deleteConfigById(Long configId);

    /**
     * 批量删除参数配置信息
     * 
     * @param configIds 需要删除的参数配置ID
     * @return 结果
     */
    public boolean deleteConfigByIds(Long[] configIds);

    /**
     * 清空缓存数据
     */
    public void clearCache();
} 