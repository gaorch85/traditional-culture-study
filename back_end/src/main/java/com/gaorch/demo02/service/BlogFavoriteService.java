package com.gaorch.demo02.service;

import com.gaorch.demo02.entity.BlogFavorite;
import com.gaorch.demo02.entity.User;
import com.gaorch.demo02.mapper.BlogFavoriteMapper;
import com.gaorch.demo02.mapper.UserMapper;
import com.gaorch.demo02.utils.JwtUtils;
import com.gaorch.demo02.utils.Result1;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogFavoriteService {

    @Autowired
    private BlogFavoriteMapper blogFavoriteMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpServletRequest request;

    public List<BlogFavorite> getFavoritessByBlogId(Integer blogId)
    {
        return blogFavoriteMapper.selectByBlogId(blogId);
    }

    public Integer getFavoriteSizeByBlogId(Integer blogId)
    {
        return blogFavoriteMapper.getFavoriteSizeByBlogId(blogId);
    }

    public Boolean deleteFavoritesByBlogId(Integer blogId)
    {
        return blogFavoriteMapper.deleteFavoritesByBlogId(blogId);
    }

    public Boolean deleteAllByUserId(Integer userId)
    {
        return blogFavoriteMapper.deleteAllByUserId(userId);
    }

    public Boolean isMyFavorite(Integer blogId, Integer userId)
    {
        System.out.println(blogFavoriteMapper.getMyFavoriteId(blogId, userId));
        return !blogFavoriteMapper.getMyFavoriteId(blogId, userId).isEmpty();
    }

    public Integer getMyFavoriteId(Integer blogId, Integer userId)
    {
        List<Integer> ids = blogFavoriteMapper.getMyFavoriteId(blogId, userId);
        if(ids.isEmpty())
            return -1;
        else
            return ids.getFirst();
    }

    public Result1 insert(Integer blogId)
    {
        User user = userMapper.selectByAccount(JwtUtils.getAccount(request));
        Integer userId = user.getId();
        BlogFavorite blogFavorite = new BlogFavorite();
        blogFavorite.setId(0);
        blogFavorite.setUserId(userId);
        blogFavorite.setBlogId(blogId);
        int i = blogFavoriteMapper.insert(blogFavorite);
        return i > 0 ? Result1.ok() : Result1.error();
    }

    public Result1 delete(Integer id)
    {
        int i = blogFavoriteMapper.deleteById(id);
        return i > 0 ? Result1.ok() : Result1.error();
    }
}
