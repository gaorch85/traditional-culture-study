package com.gaorch.demo02.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaorch.demo02.entity.BlogFavorite;
import com.gaorch.demo02.entity.BlogLike;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlogFavoriteMapper extends BaseMapper<BlogFavorite> {
    @Select("SELECT * FROM favorite WHERE blogId = #{blogId}")
    public List<BlogFavorite> selectByBlogId(Integer blogId);

    @Select("SELECT COUNT(*) FROM favorite WHERE blogId = #{blogId}")
    public Integer getFavoriteSizeByBlogId(Integer blogId);

    @Select("SELECT id FROM favorite WHERE blogId = #{blogId} AND userId = #{userId}")
    public List<Integer> getMyFavoriteId(Integer blogId, Integer userId);

    @Delete("DELETE FROM favorite WHERE blogId = #{blogId}")
    public Boolean deleteFavoritesByBlogId(Integer blogId);

    @Delete("DELETE FROM favorite WHERE userId = #{userId}")
    public Boolean deleteAllByUserId(Integer userId);

}
