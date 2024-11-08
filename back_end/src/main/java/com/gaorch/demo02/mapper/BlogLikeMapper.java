package com.gaorch.demo02.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaorch.demo02.entity.BlogLike;
import com.gaorch.demo02.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlogLikeMapper extends BaseMapper<BlogLike> {
    @Select("SELECT * FROM love WHERE blogId = #{blogId}")
    public List<BlogLike> selectByBlogId(Integer blogId);

    @Select("SELECT COUNT(*) FROM love WHERE blogId = #{blogId}")
    public Integer getLikeSizeByBlogId(Integer blogId);

    @Select("SELECT id FROM love WHERE blogId = #{blogId} AND userId = #{userId}")
    public List<Integer> getMyLikeId(Integer blogId, Integer userId);


    @Delete("DELETE FROM love WHERE blogId = #{blogId}")
    public Boolean deleteLikesByBlogId(Integer blogId);

    @Delete("DELETE FROM love WHERE userId = #{userId}")
    public Boolean deleteAllByUserId(Integer userId);

}
