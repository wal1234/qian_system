package com.qian.system.mapper;

import java.util.List;
import com.qian.system.domain.entity.BlogArticleTag;

/**
 * 文章标签关联 数据层
 */
public interface BlogArticleTagMapper {
    /**
     * 查询文章标签关联列表
     * 
     * @param articleTag 文章标签关联信息
     * @return 文章标签关联列表
     */
    public List<BlogArticleTag> selectArticleTagList(BlogArticleTag articleTag);

    /**
     * 根据文章ID查询文章标签关联
     * 
     * @param articleId 文章ID
     * @return 文章标签关联列表
     */
    public List<BlogArticleTag> selectArticleTagByArticleId(Long articleId);

    /**
     * 新增文章标签关联
     * 
     * @param articleTag 文章标签关联信息
     * @return 结果
     */
    public int insertArticleTag(BlogArticleTag articleTag);

    /**
     * 删除文章标签关联
     * 
     * @param articleId 文章ID
     * @return 结果
     */
    public int deleteArticleTagByArticleId(Long articleId);

    /**
     * 删除文章标签关联
     * 
     * @param tagId 标签ID
     * @return 结果
     */
    public int deleteArticleTagByTagId(Long tagId);
} 