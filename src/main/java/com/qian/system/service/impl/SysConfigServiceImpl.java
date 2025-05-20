package com.qian.system.service.impl;

import com.qian.system.domain.SysConfig;
import com.qian.system.mapper.SysConfigMapper;
import com.qian.system.service.ISysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 参数配置 服务层实现
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService, ApplicationListener<ContextRefreshedEvent> {
    
    @Autowired
    @Qualifier("sysConfigMapper")
    private SysConfigMapper configMapper;

    /**
     * 缓存容器
     */
    private static final Map<String, String> configCache = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(SysConfigServiceImpl.class);
    private static volatile boolean cacheLoaded = false; // 缓存加载状态标志位

    /**
     * 应用上下文初始化完成后加载缓存（解耦启动与业务初始化）
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 仅在根应用上下文初始化完成时加载缓存（避免父子容器多次触发）
        if (event.getApplicationContext().getParent() == null && !cacheLoaded) {
            log.info("检测到根应用上下文初始化完成，开始加载配置缓存...");
            // 检查Mapper是否注入成功
            if (configMapper == null) {
                log.error("SysConfigMapper未成功注入，无法加载配置缓存");
                throw new IllegalStateException("SysConfigMapper未成功注入，无法加载配置缓存");
            }
            loadingConfigCache();
            cacheLoaded = true; // 标记缓存已加载
            log.info("配置缓存加载完成");
        } else if (event.getApplicationContext().getParent() == null && cacheLoaded) {
            log.warn("配置缓存已加载，跳过重复加载操作");
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
     * 
     * @param configKey 参数键名
     * @return 参数配置信息
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = configCache.get(configKey);
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
            configCache.put(configKey, value);
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
                configCache.put(config.getConfigKey(), value);
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
            configCache.remove(temp.getConfigKey());
        }
        
        int row = configMapper.updateConfig(config);
        if (row > 0) {
            String value = config.getConfigValue();
            if (value == null) {
                log.warn("更新配置项{}的configValue为null，不放入缓存", config.getConfigKey());
            } else {
                configCache.put(config.getConfigKey(), value);
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
            configCache.remove(config.getConfigKey());
        }
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache() {
        try {
            List<SysConfig> configsList = configMapper.selectConfigList(new SysConfig());
            // 避免数据库查询结果为空导致NPE
            if (configsList != null) {
                for (SysConfig config : configsList) {
                    String configKey = config.getConfigKey();
                    if (configKey == null) {
                        log.warn("检测到配置项configKey为null，跳过该配置：{}", config);
                        continue;
                    }
                    configCache.put(configKey, config.getConfigValue());
                }
                log.info("成功加载{}条配置到缓存", configsList.size());
            } else {
                log.warn("数据库查询返回空配置列表，缓存未更新");
            }
        } catch (Exception e) {
            log.error("加载配置缓存时发生数据库异常", e);
            throw new RuntimeException("加载配置缓存失败", e);
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache() {
        configCache.clear();
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