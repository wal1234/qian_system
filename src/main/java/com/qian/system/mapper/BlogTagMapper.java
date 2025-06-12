package com.qian.system.mapper;

import java.util.List;
import com.qian.system.domain.entity.BlogTag;

/**
 * 标签管理 数据层
 */
public interface BlogTagMapper {
    /**
     * 查询标签列表
     * 
     * @param tag 标签信息
     * @return 标签集合
     */
    public List<BlogTag> selectTagList(BlogTag tag);

    /**
     * 查询标签详细
     * 
     * @param tagId 标签ID
     * @return 标签信息
     */
    public BlogTag selectTagById(Long tagId);

    /**
     * 新增标签
     * 
     * @param tag 标签信息
     * @return 结果
     */
    public int insertTag(BlogTag tag);

    /**
     * 修改标签
     * 
     * @param tag 标签信息
     * @return 结果
     */
    public int updateTag(BlogTag tag);

    /**
     * 批量删除标签
     * 
     * @param tagIds 需要删除的标签ID
     * @return 结果
     */
    public int deleteTagByIds(Long[] tagIds);

    /**
     * 删除标签信息
     * 
     * @param tagId 标签ID
     * @return 结果
     */
    public int deleteTagById(Long tagId);

    /**
     * 修改标签状态
     * 
     * @param tag 标签信息
     * @return 结果
     */
    public int updateTagStatus(BlogTag tag);
} 