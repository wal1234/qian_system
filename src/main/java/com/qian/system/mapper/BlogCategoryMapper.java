package com.qian.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.qian.system.domain.entity.BlogCategory;

/**
 * 分类管理 数据层
 */
@Mapper
public interface BlogCategoryMapper {
    /**
     * 查询分类列表
     * 
     * @param category 分类信息
     * @return 分类列表
     */
    List<BlogCategory> selectCategoryList(BlogCategory category);

    /**
     * 查询分类信息
     * 
     * @param categoryId 分类ID
     * @return 分类信息
     */
    BlogCategory selectCategoryById(Long categoryId);

    /**
     * 新增分类
     * 
     * @param category 分类信息
     * @return 结果
     */
    int insertCategory(BlogCategory category);

    /**
     * 修改分类
     * 
     * @param category 分类信息
     * @return 结果
     */
    int updateCategory(BlogCategory category);

    /**
     * 批量删除分类
     * 
     * @param categoryIds 需要删除的分类ID
     * @return 结果
     */
    int deleteCategoryByIds(Long[] categoryIds);

    /**
     * 修改分类状态
     * 
     * @param category 分类信息
     * @return 结果
     */
    int updateCategoryStatus(BlogCategory category);
} 