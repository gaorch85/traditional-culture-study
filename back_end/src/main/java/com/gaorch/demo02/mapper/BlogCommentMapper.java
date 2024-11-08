package com.gaorch.demo02.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaorch.demo02.entity.BlogComment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlogCommentMapper extends BaseMapper<BlogComment> {
    @Select("SELECT * FROM comment WHERE blogId = #{blogId}")
    public List<BlogComment> selectByBlogId(Integer blogId);

    @Select("SELECT COUNT(*) FROM comment WHERE blogId = #{blogId}")
    public Integer getCommentSizeByBlogId(Integer blogId);

    @Delete("DELETE FROM comment WHERE blogId = #{blogId}")
    public Boolean deleteCommentsByBlogId(Integer blogId);

    @Delete("DELETE FROM comment WHERE userId = #{userId}")
    public Boolean deleteAllByUserId(Integer userId);
}
