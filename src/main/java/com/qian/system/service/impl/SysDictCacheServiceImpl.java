package com.qian.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.qian.common.constant.CacheConstants;
import com.qian.system.domain.entity.SysDictData;
import com.qian.system.service.ISysDictCacheService;

/**
 * 字典缓存服务实现类
 */
@Service
public class SysDictCacheServiceImpl implements ISysDictCacheService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取字典缓存
     * 
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<SysDictData> getDictCache(String dictType) {
        String cacheKey = getCacheKey(dictType);
        return (List<SysDictData>) redisTemplate.opsForValue().get(cacheKey);
    }

    /**
     * 设置字典缓存
     * 
     * @param dictType 字典类型
     * @param dictDataList 字典数据列表
     */
    @Override
    public void setDictCache(String dictType, List<SysDictData> dictDataList) {
        String cacheKey = getCacheKey(dictType);
        redisTemplate.opsForValue().set(cacheKey, dictDataList);
    }

    /**
     * 删除字典缓存
     * 
     * @param dictType 字典类型
     */
    @Override
    public void removeDictCache(String dictType) {
        String cacheKey = getCacheKey(dictType);
        redisTemplate.delete(cacheKey);
    }

    /**
     * 清空字典缓存
     */
    @Override
    public void clearDictCache() {
        String pattern = CacheConstants.SYS_DICT_KEY + "*";
        redisTemplate.delete(redisTemplate.keys(pattern));
    }

    /**
     * 获取cache key
     * 
     * @param dictType 字典类型
     * @return 缓存键key
     */
    private String getCacheKey(String dictType) {
        return CacheConstants.SYS_DICT_KEY + dictType;
    }
} 