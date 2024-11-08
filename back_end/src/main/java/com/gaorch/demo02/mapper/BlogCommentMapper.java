package com.gaorch.demo02.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaorch.demo02.entity.Comment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlogCommentMapper extends BaseMapper<Comment> {
    @Select("SELECT * FROM comment WHERE blog_id = #{blogId}")
    public List<Comment> selectByBlogId(Integer blogId);

    @Select("SELECT COUNT(*) FROM comment WHERE blog_id = #{blogId}")
    public Integer getCommentSizeByBlogId(Integer blogId);

    @Delete("DELETE FROM comment WHERE blog_id = #{blogId}")
    public Boolean deleteCommentsByBlogId(Integer blogId);

    @Delete("DELETE FROM comment WHERE user_id = #{userId}")
    public Boolean deleteAllByUserId(Integer userId);
}
