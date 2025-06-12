package com.qian.system.service;

import java.util.List;
import com.qian.system.domain.entity.BlogArticle;

/**
 * 文章管理 服务接口
 */
public interface IBlogArticleService {
    /**
     * 查询文章列表
     * 
     * @param article 文章信息
     * @return 文章集合
     */
    List<BlogArticle> selectArticleList(BlogArticle article);

    /**
     * 查询文章详细
     * 
     * @param articleId 文章ID
     * @return 文章对象
     */
    BlogArticle selectArticleById(Long articleId);

    /**
     * 新增文章
     * 
     * @param article 文章信息
     * @return 结果
     */
    int insertArticle(BlogArticle article);

    /**
     * 修改文章
     * 
     * @param article 文章信息
     * @return 结果
     */
    int updateArticle(BlogArticle article);

    /**
     * 批量删除文章
     * 
     * @param articleIds 需要删除的文章ID
     * @return 结果
     */
    int deleteArticleByIds(Long[] articleIds);

    /**
     * 删除文章信息
     * 
     * @param articleId 文章ID
     * @return 结果
     */
    int deleteArticleById(Long articleId);

    /**
     * 修改文章状态
     * 
     * @param article 文章信息
     * @return 结果
     */
    int updateArticleStatus(BlogArticle article);

    /**
     * 修改文章置顶状态
     * 
     * @param article 文章信息
     * @return 结果
     */
    int updateArticleTop(BlogArticle article);

    /**
     * 修改文章推荐状态
     * 
     * @param article 文章信息
     * @return 结果
     */
    int updateArticleRecommend(BlogArticle article);

    /**
     * 增加文章浏览量
     * 
     * @param articleId 文章ID
     * @return 结果
     */
    int incrementViewCount(Long articleId);

    /**
     * 增加文章点赞量
     * 
     * @param articleId 文章ID
     * @return 结果
     */
    int incrementLikeCount(Long articleId);

    /**
     * 增加文章评论量
     * 
     * @param articleId 文章ID
     * @return 结果
     */
    int incrementCommentCount(Long articleId);
} 