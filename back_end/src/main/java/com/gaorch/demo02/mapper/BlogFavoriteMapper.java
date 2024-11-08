package com.gaorch.demo02.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaorch.demo02.entity.Favorite;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlogFavoriteMapper extends BaseMapper<Favorite> {
    @Select("SELECT * FROM favorite WHERE blog_id = #{blogId}")
    public List<Favorite> selectByBlogId(Integer blogId);

    @Select("SELECT COUNT(*) FROM favorite WHERE blog_id = #{blogId}")
    public Integer getFavoriteSizeByBlogId(Integer blogId);

    @Select("SELECT id FROM favorite WHERE blog_id = #{blogId} AND user_id = #{userId}")
    public List<Integer> getMyFavoriteId(Integer blogId, Integer userId);

    @Delete("DELETE FROM favorite WHERE blog_id = #{blogId}")
    public Boolean deleteFavoritesByBlogId(Integer blogId);

    @Delete("DELETE FROM favorite WHERE user_id = #{userId}")
    public Boolean deleteAllByUserId(Integer userId);

}
