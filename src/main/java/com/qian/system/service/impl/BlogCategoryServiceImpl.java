package com.qian.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qian.system.mapper.BlogCategoryMapper;
import com.qian.system.domain.entity.BlogCategory;
import com.qian.system.service.IBlogCategoryService;

/**
 * 分类管理 服务实现类
 */
@Service
public class BlogCategoryServiceImpl implements IBlogCategoryService {
    @Autowired
    private BlogCategoryMapper categoryMapper;

    /**
     * 查询分类列表
     * 
     * @param category 分类信息
     * @return 分类列表
     */
    @Override
    public List<BlogCategory> selectCategoryList(BlogCategory category) {
        return categoryMapper.selectCategoryList(category);
    }

    /**
     * 查询分类信息
     * 
     * @param categoryId 分类ID
     * @return 分类信息
     */
    @Override
    public BlogCategory selectCategoryById(Long categoryId) {
        return categoryMapper.selectCategoryById(categoryId);
    }

    /**
     * 新增分类
     * 
     * @param category 分类信息
     * @return 结果
     */
    @Override
    public int insertCategory(BlogCategory category) {
        return categoryMapper.insertCategory(category);
    }

    /**
     * 修改分类
     * 
     * @param category 分类信息
     * @return 结果
     */
    @Override
    public int updateCategory(BlogCategory category) {
        return categoryMapper.updateCategory(category);
    }

    /**
     * 批量删除分类
     * 
     * @param categoryIds 需要删除的分类ID
     * @return 结果
     */
    @Override
    public int deleteCategoryByIds(Long[] categoryIds) {
        return categoryMapper.deleteCategoryByIds(categoryIds);
    }

    /**
     * 修改分类状态
     * 
     * @param category 分类信息
     * @return 结果
     */
    @Override
    public int updateCategoryStatus(BlogCategory category) {
        return categoryMapper.updateCategoryStatus(category);
    }
} 