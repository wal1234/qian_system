package com.qian.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qian.system.mapper.BlogCommentMapper;
import com.qian.system.domain.entity.BlogComment;
import com.qian.system.service.IBlogCommentService;

/**
 * 评论管理 服务实现
 */
@Service
public class BlogCommentServiceImpl implements IBlogCommentService {
    @Autowired
    private BlogCommentMapper commentMapper;

    /**
     * 查询评论列表
     * 
     * @param comment 评论信息
     * @return 评论列表
     */
    @Override
    public List<BlogComment> selectCommentList(BlogComment comment) {
        return commentMapper.selectCommentList(comment);
    }

    /**
     * 查询评论信息
     * 
     * @param commentId 评论ID
     * @return 评论信息
     */
    @Override
    public BlogComment selectCommentById(Long commentId) {
        return commentMapper.selectCommentById(commentId);
    }

    /**
     * 新增评论
     * 
     * @param comment 评论信息
     * @return 结果
     */
    @Override
    public int insertComment(BlogComment comment) {
        return commentMapper.insertComment(comment);
    }

    /**
     * 修改评论
     * 
     * @param comment 评论信息
     * @return 结果
     */
    @Override
    public int updateComment(BlogComment comment) {
        return commentMapper.updateComment(comment);
    }

    /**
     * 批量删除评论
     * 
     * @param commentIds 需要删除的评论ID
     * @return 结果
     */
    @Override
    public int deleteCommentByIds(Long[] commentIds) {
        return commentMapper.deleteCommentByIds(commentIds);
    }

    /**
     * 修改评论状态
     * 
     * @param comment 评论信息
     * @return 结果
     */
    @Override
    public int updateCommentStatus(BlogComment comment) {
        return commentMapper.updateCommentStatus(comment);
    }

    /**
     * 增加评论点赞数
     * 
     * @param commentId 评论ID
     * @return 结果
     */
    @Override
    public int incrementLikeCount(Long commentId) {
        return commentMapper.incrementLikeCount(commentId);
    }
} 