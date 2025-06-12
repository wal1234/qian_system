package com.qian.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qian.system.mapper.BlogArticleMapper;
import com.qian.system.mapper.BlogArticleTagMapper;
import com.qian.system.domain.entity.BlogArticle;
import com.qian.system.domain.entity.BlogArticleTag;
import com.qian.system.service.IBlogArticleService;
import lombok.extern.slf4j.Slf4j;

/**
 * 文章管理 服务层实现
 */
@Slf4j
@Service
public class BlogArticleServiceImpl implements IBlogArticleService {
    @Autowired
    private BlogArticleMapper articleMapper;

    @Autowired
    private BlogArticleTagMapper articleTagMapper;

    /**
     * 查询文章列表
     * 
     * @param article 文章信息
     * @return 文章集合
     */
    @Override
    public List<BlogArticle> selectArticleList(BlogArticle article) {
        return articleMapper.selectArticleList(article);
    }

    /**
     * 查询文章详细
     * 
     * @param articleId 文章ID
     * @return 文章信息
     */
    @Override
    public BlogArticle selectArticleById(Long articleId) {
        return articleMapper.selectArticleById(articleId);
    }

    /**
     * 新增文章
     * 
     * @param article 文章信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertArticle(BlogArticle article) {
        // 新增文章信息
        int rows = articleMapper.insertArticle(article);
        // 新增文章标签关联
        insertArticleTag(article);
        return rows;
    }

    /**
     * 修改文章
     * 
     * @param article 文章信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateArticle(BlogArticle article) {
        // 修改文章信息
        int rows = articleMapper.updateArticle(article);
        // 删除文章标签关联
        articleTagMapper.deleteArticleTagByArticleId(article.getArticleId());
        // 新增文章标签关联
        insertArticleTag(article);
        return rows;
    }

    /**
     * 新增文章标签信息
     * 
     * @param article 文章对象
     */
    public void insertArticleTag(BlogArticle article) {
        Long[] tagIds = article.getTagIds();
        if (tagIds != null && tagIds.length > 0) {
            // 新增文章标签关联
            for (Long tagId : tagIds) {
                BlogArticleTag articleTag = new BlogArticleTag();
                articleTag.setArticleId(article.getArticleId());
                articleTag.setTagId(tagId);
                articleTagMapper.insertArticleTag(articleTag);
            }
        }
    }

    /**
     * 批量删除文章
     * 
     * @param articleIds 需要删除的文章ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteArticleByIds(Long[] articleIds) {
        for (Long articleId : articleIds) {
            // 删除文章标签关联
            articleTagMapper.deleteArticleTagByArticleId(articleId);
        }
        return articleMapper.deleteArticleByIds(articleIds);
    }

    /**
     * 删除文章信息
     * 
     * @param articleId 文章ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteArticleById(Long articleId) {
        // 删除文章标签关联
        articleTagMapper.deleteArticleTagByArticleId(articleId);
        return articleMapper.deleteArticleById(articleId);
    }

    /**
     * 更新文章状态
     * 
     * @param article 文章信息
     * @return 结果
     */
    @Override
    public int updateArticleStatus(BlogArticle article) {
        return articleMapper.updateArticle(article);
    }

    /**
     * 更新文章置顶状态
     * 
     * @param article 文章信息
     * @return 结果
     */
    @Override
    public int updateArticleTop(BlogArticle article) {
        return articleMapper.updateArticle(article);
    }

    /**
     * 更新文章推荐状态
     * 
     * @param article 文章信息
     * @return 结果
     */
    @Override
    public int updateArticleRecommend(BlogArticle article) {
        return articleMapper.updateArticle(article);
    }

    /**
     * 增加文章浏览量
     * 
     * @param articleId 文章ID
     * @return 结果
     */
    @Override
    public int incrementViewCount(Long articleId) {
        return articleMapper.incrementViewCount(articleId);
    }

    /**
     * 增加文章点赞数
     * 
     * @param articleId 文章ID
     * @return 结果
     */
    @Override
    public int incrementLikeCount(Long articleId) {
        return articleMapper.incrementLikeCount(articleId);
    }

    /**
     * 增加文章评论数
     * 
     * @param articleId 文章ID
     * @return 结果
     */
    @Override
    public int incrementCommentCount(Long articleId) {
        return articleMapper.incrementCommentCount(articleId);
    }
} 