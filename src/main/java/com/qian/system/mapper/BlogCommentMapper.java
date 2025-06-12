package com.qian.system.mapper;

import java.util.List;
import com.qian.system.domain.entity.BlogComment;

/**
 * 评论管理 数据层
 */
public interface BlogCommentMapper {
    /**
     * 查询评论列表
     * 
     * @param comment 评论信息
     * @return 评论列表
     */
    List<BlogComment> selectCommentList(BlogComment comment);

    /**
     * 查询评论信息
     * 
     * @param commentId 评论ID
     * @return 评论信息
     */
    BlogComment selectCommentById(Long commentId);

    /**
     * 新增评论
     * 
     * @param comment 评论信息
     * @return 结果
     */
    int insertComment(BlogComment comment);

    /**
     * 修改评论
     * 
     * @param comment 评论信息
     * @return 结果
     */
    int updateComment(BlogComment comment);

    /**
     * 批量删除评论
     * 
     * @param commentIds 需要删除的评论ID
     * @return 结果
     */
    int deleteCommentByIds(Long[] commentIds);

    /**
     * 修改评论状态
     * 
     * @param comment 评论信息
     * @return 结果
     */
    int updateCommentStatus(BlogComment comment);

    /**
     * 增加评论点赞数
     * 
     * @param commentId 评论ID
     * @return 结果
     */
    int incrementLikeCount(Long commentId);
} 