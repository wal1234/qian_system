package com.qian.system.mapper;

import java.util.List;
import com.qian.system.domain.entity.BlogArticle;

/**
 * 文章管理 数据层
 */
public interface BlogArticleMapper {
    /**
     * 查询文章列表
     * 
     * @param article 文章信息
     * @return 文章集合
     */
    public List<BlogArticle> selectArticleList(BlogArticle article);

    /**
     * 查询文章详细
     * 
     * @param articleId 文章ID
     * @return 文章信息
     */
    public BlogArticle selectArticleById(Long articleId);

    /**
     * 新增文章
     * 
     * @param article 文章信息
     * @return 结果
     */
    public int insertArticle(BlogArticle article);

    /**
     * 修改文章
     * 
     * @param article 文章信息
     * @return 结果
     */
    public int updateArticle(BlogArticle article);

    /**
     * 批量删除文章
     * 
     * @param articleIds 需要删除的文章ID
     * @return 结果
     */
    public int deleteArticleByIds(Long[] articleIds);

    /**
     * 删除文章信息
     * 
     * @param articleId 文章ID
     * @return 结果
     */
    public int deleteArticleById(Long articleId);

    /**
     * 增加文章浏览量
     * 
     * @param articleId 文章ID
     * @return 结果
     */
    public int incrementViewCount(Long articleId);

    /**
     * 增加文章点赞数
     * 
     * @param articleId 文章ID
     * @return 结果
     */
    public int incrementLikeCount(Long articleId);

    /**
     * 增加文章评论数
     * 
     * @param articleId 文章ID
     * @return 结果
     */
    public int incrementCommentCount(Long articleId);
} 