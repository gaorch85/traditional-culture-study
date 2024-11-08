package com.gaorch.demo02.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaorch.demo02.entity.Love;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlogLikeMapper extends BaseMapper<Love> {
    @Select("SELECT * FROM love WHERE blog_id = #{blogId}")
    public List<Love> selectByBlogId(Integer blogId);

    @Select("SELECT COUNT(*) FROM love WHERE blog_id = #{blogId}")
    public Integer getLikeSizeByBlogId(Integer blogId);

    @Select("SELECT id FROM love WHERE blog_id = #{blogId} AND user_id = #{userId}")
    public List<Integer> getMyLikeId(Integer blogId, Integer userId);


    @Delete("DELETE FROM love WHERE blog_id = #{blogId}")
    public Boolean deleteLikesByBlogId(Integer blogId);

    @Delete("DELETE FROM love WHERE user_id = #{userId}")
    public Boolean deleteAllByUserId(Integer userId);

}
