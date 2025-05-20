package com.qian.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.qian.system.domain.entity.SysPost;

/**
 * 岗位信息 数据层
 */
@Mapper
public interface SysPostMapper {
    /**
     * 根据用户名查询岗位
     * 
     * @param userName 用户名
     * @return 岗位列表
     */
    public List<SysPost> selectPostsByUserName(String userName);
} 