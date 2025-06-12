package com.qian.system.service;

import java.util.List;
import com.qian.system.domain.entity.BlogTag;

/**
 * 标签管理 服务接口
 */
public interface IBlogTagService {
    /**
     * 查询标签列表
     * 
     * @param tag 标签信息
     * @return 标签列表
     */
    List<BlogTag> selectTagList(BlogTag tag);

    /**
     * 查询标签信息
     * 
     * @param tagId 标签ID
     * @return 标签信息
     */
    BlogTag selectTagById(Long tagId);

    /**
     * 新增标签
     * 
     * @param tag 标签信息
     * @return 结果
     */
    int insertTag(BlogTag tag);

    /**
     * 修改标签
     * 
     * @param tag 标签信息
     * @return 结果
     */
    int updateTag(BlogTag tag);

    /**
     * 批量删除标签
     * 
     * @param tagIds 需要删除的标签ID
     * @return 结果
     */
    int deleteTagByIds(Long[] tagIds);

    /**
     * 修改标签状态
     * 
     * @param tag 标签信息
     * @return 结果
     */
    int updateTagStatus(BlogTag tag);
} 