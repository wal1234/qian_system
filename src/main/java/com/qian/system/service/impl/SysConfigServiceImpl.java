package com.qian.system.service.impl;

import com.qian.system.domain.SysConfig;
import com.qian.system.mapper.SysConfigMapper;
import com.qian.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 参数配置 服务层实现
 */
@Slf4j
@Service
public class SysConfigServiceImpl implements ISysConfigService, ApplicationListener<ContextRefreshedEvent> {
    
    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String CONFIG_CACHE_KEY = "sys:config:";
    private static final long CONFIG_CACHE_EXPIRE = 24; // 缓存过期时间（小时）

    /**
     * 应用上下文初始化完成后加载缓存
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            log.info("检测到根应用上下文初始化完成，开始加载配置缓存...");
            if (configMapper == null) {
                log.error("SysConfigMapper未成功注入，无法加载配置缓存");
                throw new IllegalStateException("SysConfigMapper未成功注入，无法加载配置缓存");
            }
            loadingConfigCache();
            log.info("配置缓存加载完成");
        }
    }

    /**
     * 查询参数配置信息
     * 
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public SysConfig selectConfigById(Long configId) {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return configMapper.selectConfigById(configId);
    }

    /**
     * 根据键名查询参数配置信息
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String cacheKey = CONFIG_CACHE_KEY + configKey;
        String configValue = redisTemplate.opsForValue().get(cacheKey);
        if (configValue != null) {
            return configValue;
        }
        SysConfig config = configMapper.selectConfigByKey(configKey);
        if (config != null) {
            String value = config.getConfigValue();
            if (value == null) {
                log.warn("配置项{}的configValue为null，不放入缓存", configKey);
                return "";
            }
            redisTemplate.opsForValue().set(cacheKey, value, CONFIG_CACHE_EXPIRE, TimeUnit.HOURS);
            return value;
        }
        return "";
    }

    /**
     * 获取验证码开关
     * 
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (captchaEnabled != null) {
            return Boolean.parseBoolean(captchaEnabled);
        }
        return true;
    }

    /**
     * 查询参数配置列表
     * 
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config) {
        return configMapper.selectConfigList(config);
    }

    /**
     * 新增参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config) {
        int row = configMapper.insertConfig(config);
        if (row > 0) {
            String value = config.getConfigValue();
            if (value == null) {
                log.warn("新增配置项{}的configValue为null，不放入缓存", config.getConfigKey());
            } else {
                redisTemplate.opsForValue().set(CONFIG_CACHE_KEY + config.getConfigKey(), value, CONFIG_CACHE_EXPIRE, TimeUnit.HOURS);
            }
        }
        return row;
    }

    /**
     * 修改参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysConfig config) {
        SysConfig temp = configMapper.selectConfigById(config.getConfigId());
        if (temp != null) {
            redisTemplate.delete(CONFIG_CACHE_KEY + temp.getConfigKey());
        }
        
        int row = configMapper.updateConfig(config);
        if (row > 0) {
            String value = config.getConfigValue();
            if (value == null) {
                log.warn("更新配置项{}的configValue为null，不放入缓存", config.getConfigKey());
            } else {
                redisTemplate.opsForValue().set(CONFIG_CACHE_KEY + config.getConfigKey(), value, CONFIG_CACHE_EXPIRE, TimeUnit.HOURS);
            }
        }
        return row;
    }

    /**
     * 批量删除参数配置对象
     * 
     * @param configIds 需要删除的参数配置ID
     */
    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = selectConfigById(configId);
            if (config == null) {
                log.warn("配置ID{}不存在，跳过删除操作", configId);
                continue;
            }
            if ("Y".equals(config.getConfigType())) {
                throw new RuntimeException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            configMapper.deleteConfigById(configId);
            redisTemplate.delete(CONFIG_CACHE_KEY + config.getConfigKey());
        }
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache() {
        try {
            List<SysConfig> configsList = configMapper.selectConfigList(new SysConfig());
            if (configsList != null) {
                for (SysConfig config : configsList) {
                    String configKey = config.getConfigKey();
                    if (configKey == null) {
                        log.warn("检测到配置项configKey为null，跳过该配置：{}", config);
                        continue;
                    }
                    String value = config.getConfigValue();
                    if (value != null) {
                        redisTemplate.opsForValue().set(CONFIG_CACHE_KEY + configKey, value, CONFIG_CACHE_EXPIRE, TimeUnit.HOURS);
                    }
                }
                log.info("成功加载{}条配置到缓存", configsList.size());
            } else {
                log.warn("数据库查询返回空配置列表，缓存未更新");
            }
        } catch (Exception e) {
            log.error("加载配置缓存时发生异常", e);
            throw new RuntimeException("加载配置缓存失败", e);
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache() {
        try {
            redisTemplate.delete(redisTemplate.keys(CONFIG_CACHE_KEY + "*"));
            log.info("配置缓存清空成功");
        } catch (Exception e) {
            log.error("清空配置缓存时发生异常", e);
            throw new RuntimeException("清空配置缓存失败", e);
        }
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public String checkConfigKeyUnique(SysConfig config) {
        Long configId = config.getConfigId() == null ? -1L : config.getConfigId();
        SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (info != null && info.getConfigId().longValue() != configId.longValue()) {
            return "1";
        }
        return "0";
    }
}