package com.qian.system.service;

import java.util.List;
import com.qian.system.domain.entity.SysDictData;

/**
 * 字典缓存服务接口
 */
public interface ISysDictCacheService {
    /**
     * 获取字典缓存
     * 
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    List<SysDictData> getDictCache(String dictType);

    /**
     * 设置字典缓存
     * 
     * @param dictType 字典类型
     * @param dictDataList 字典数据列表
     */
    void setDictCache(String dictType, List<SysDictData> dictDataList);

    /**
     * 删除字典缓存
     * 
     * @param dictType 字典类型
     */
    void removeDictCache(String dictType);

    /**
     * 清空字典缓存
     */
    void clearDictCache();
} 