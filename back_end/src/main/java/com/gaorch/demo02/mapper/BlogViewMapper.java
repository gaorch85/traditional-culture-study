package com.gaorch.demo02.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaorch.demo02.entity.BlogView;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

public interface BlogViewMapper extends BaseMapper<BlogView> {
    @Select("SELECT COUNT(*) FROM view WHERE blogId = #{blogId}")
    public Integer getViewSizeByBlogId(Integer blogId);

    @Delete("DELETE FROM view WHERE blogId = #{blogId}")
    public Boolean deleteByBlogId(Integer blogId);

    @Delete("DELETE FROM view WHERE userId = #{userId}")
    public Boolean deleteByUserId(Integer userId);

    @Select("SELECT * FROM view WHERE blogId = #{blogId} AND userId = #{userId}")
    public BlogView getBlogViewByBlogIdAndUserId(Integer blogId, Integer userId);
}