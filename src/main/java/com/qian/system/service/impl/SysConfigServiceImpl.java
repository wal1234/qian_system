package com.qian.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qian.common.exception.ServiceException;
import com.qian.common.utils.SecurityUtils;
import com.qian.common.utils.StringUtils;
import com.qian.system.domain.entity.SysConfig;
import com.qian.system.exception.ConfigException;
import com.qian.system.mapper.SysConfigMapper;
import com.qian.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 参数配置 服务层实现
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 查询参数配置列表
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config) {
        try {
            LambdaQueryWrapper<SysConfig> lqw = new LambdaQueryWrapper<>();
            lqw.like(StringUtils.isNotBlank(config.getConfigName()), SysConfig::getConfigName, config.getConfigName())
               .like(StringUtils.isNotBlank(config.getConfigKey()), SysConfig::getConfigKey, config.getConfigKey())
               .eq(StringUtils.isNotBlank(config.getConfigType()), SysConfig::getConfigType, config.getConfigType());
            return list(lqw);
        } catch (Exception e) {
            throw new ConfigException("查询参数配置列表失败", e);
        }
    }

    /**
     * 查询参数配置信息
     */
    @Override
    public SysConfig selectConfig(SysConfig config) {
        try {
            LambdaQueryWrapper<SysConfig> lqw = new LambdaQueryWrapper<>();
            lqw.eq(StringUtils.isNotBlank(config.getConfigKey()), SysConfig::getConfigKey, config.getConfigKey());
            return getOne(lqw);
        } catch (Exception e) {
            throw new ConfigException("查询参数配置信息失败", e);
        }
    }

    /**
     * 根据键名查询参数配置信息
     */
    @Override
    public String selectConfigByKey(String configKey) {
        try {
            String configValue = (String) redisTemplate.opsForValue().get(getCacheKey(configKey));
            if (StringUtils.isNotEmpty(configValue)) {
                return configValue;
            }
            LambdaQueryWrapper<SysConfig> lqw = new LambdaQueryWrapper<>();
            lqw.eq(SysConfig::getConfigKey, configKey);
            SysConfig config = getOne(lqw);
            if (StringUtils.isNotNull(config)) {
                redisTemplate.opsForValue().set(getCacheKey(configKey), config.getConfigValue());
                return config.getConfigValue();
            }
            return StringUtils.EMPTY;
        } catch (Exception e) {
            throw new ConfigException("根据键名查询参数配置信息失败", e);
        }
    }

    /**
     * 校验参数键名是否唯一
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfig config) {
        try {
            Long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
            LambdaQueryWrapper<SysConfig> lqw = new LambdaQueryWrapper<>();
            lqw.eq(SysConfig::getConfigKey, config.getConfigKey());
            SysConfig info = getOne(lqw);
            if (StringUtils.isNotNull(info) && info.getConfigId().longValue() != configId.longValue()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new ConfigException("校验参数键名是否唯一失败", e);
        }
    }

    /**
     * 新增参数配置
     */
    @Override
    public boolean insertConfig(SysConfig config) {
        try {
            config.setCreateBy(SecurityUtils.getUsername());
            return save(config);
        } catch (Exception e) {
            throw new ConfigException("新增参数配置失败", e);
        }
    }

    /**
     * 修改参数配置
     */
    @Override
    public boolean updateConfig(SysConfig config) {
        try {
            config.setUpdateBy(SecurityUtils.getUsername());
            boolean result = updateById(config);
            if (result) {
                redisTemplate.delete(getCacheKey(config.getConfigKey()));
            }
            return result;
        } catch (Exception e) {
            throw new ConfigException("修改参数配置失败", e);
        }
    }

    /**
     * 删除参数配置信息
     */
    @Override
    public boolean deleteConfigById(Long configId) {
        try {
            SysConfig config = getById(configId);
            if (StringUtils.equals("Y", config.getConfigType())) {
                throw new ServiceException("内置参数不能删除");
            }
            boolean result = removeById(configId);
            if (result) {
                redisTemplate.delete(getCacheKey(config.getConfigKey()));
            }
            return result;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigException("删除参数配置信息失败", e);
        }
    }

    /**
     * 批量删除参数配置信息
     */
    @Override
    public boolean deleteConfigByIds(Long[] configIds) {
        try {
            for (Long configId : configIds) {
                SysConfig config = getById(configId);
                if (StringUtils.equals("Y", config.getConfigType())) {
                    throw new ServiceException(String.format("内置参数【%1$s】不能删除", config.getConfigName()));
                }
            }
            boolean result = removeByIds(List.of(configIds));
            if (result) {
                for (Long configId : configIds) {
                    SysConfig config = getById(configId);
                    redisTemplate.delete(getCacheKey(config.getConfigKey()));
                }
            }
            return result;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigException("批量删除参数配置信息失败", e);
        }
    }

    /**
     * 清空缓存数据
     */
    @Override
    public void clearCache() {
        try {
            List<SysConfig> configs = list();
            for (SysConfig config : configs) {
                redisTemplate.delete(getCacheKey(config.getConfigKey()));
            }
        } catch (Exception e) {
            throw new ConfigException("清空缓存数据失败", e);
        }
    }

    /**
     * 获取cache key
     */
    private String getCacheKey(String configKey) {
        return "sys_config:" + configKey;
    }
}