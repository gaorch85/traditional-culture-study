package com.gaorch.demo02.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaorch.demo02.entity.View;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

public interface BlogViewMapper extends BaseMapper<View> {
    @Select("SELECT COUNT(*) FROM view WHERE blog_id = #{blogId}")
    public Integer getViewSizeByBlogId(Integer blogId);

    @Delete("DELETE FROM view WHERE blog_id = #{blogId}")
    public Boolean deleteByBlogId(Integer blogId);

    @Delete("DELETE FROM view WHERE user_id = #{userId}")
    public Boolean deleteByUserId(Integer userId);

    @Select("SELECT * FROM view WHERE blog_id = #{blogId} AND user_id = #{userId}")
    public View getBlogViewByBlogIdAndUserId(Integer blogId, Integer userId);
}