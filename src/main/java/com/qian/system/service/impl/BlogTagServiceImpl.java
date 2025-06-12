package com.qian.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qian.system.mapper.BlogTagMapper;
import com.qian.system.mapper.BlogArticleTagMapper;
import com.qian.system.domain.entity.BlogTag;
import com.qian.system.service.IBlogTagService;
import lombok.extern.slf4j.Slf4j;

/**
 * 标签管理 服务层实现
 */
@Slf4j
@Service
public class BlogTagServiceImpl implements IBlogTagService {
    @Autowired
    private BlogTagMapper tagMapper;

    @Autowired
    private BlogArticleTagMapper articleTagMapper;

    /**
     * 查询标签列表
     * 
     * @param tag 标签信息
     * @return 标签集合
     */
    @Override
    public List<BlogTag> selectTagList(BlogTag tag) {
        return tagMapper.selectTagList(tag);
    }

    /**
     * 查询标签详细
     * 
     * @param tagId 标签ID
     * @return 标签信息
     */
    @Override
    public BlogTag selectTagById(Long tagId) {
        return tagMapper.selectTagById(tagId);
    }

    /**
     * 新增标签
     * 
     * @param tag 标签信息
     * @return 结果
     */
    @Override
    public int insertTag(BlogTag tag) {
        return tagMapper.insertTag(tag);
    }

    /**
     * 修改标签
     * 
     * @param tag 标签信息
     * @return 结果
     */
    @Override
    public int updateTag(BlogTag tag) {
        return tagMapper.updateTag(tag);
    }

    /**
     * 批量删除标签
     * 
     * @param tagIds 需要删除的标签ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTagByIds(Long[] tagIds) {
        for (Long tagId : tagIds) {
            // 删除文章标签关联
            articleTagMapper.deleteArticleTagByTagId(tagId);
        }
        return tagMapper.deleteTagByIds(tagIds);
    }

    /**
     * 修改标签状态
     * 
     * @param tag 标签信息
     * @return 结果
     */
    @Override
    public int updateTagStatus(BlogTag tag) {
        return tagMapper.updateTagStatus(tag);
    }
} 