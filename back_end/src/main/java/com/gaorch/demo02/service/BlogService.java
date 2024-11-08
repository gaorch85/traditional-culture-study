package com.gaorch.demo02.service;

import com.gaorch.demo02.entity.Blog;
import com.gaorch.demo02.entity.User;
import com.gaorch.demo02.mapper.BlogMapper;
import com.gaorch.demo02.mapper.UserMapper;
import com.gaorch.demo02.utils.Result1;
import com.gaorch.demo02.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class BlogService
{
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogLikeService blogLikeService;
    @Autowired
    private BlogFavoriteService blogFavoriteService;
    @Autowired
    private BlogCommentService blogCommentService;
    @Autowired
    private BlogViewService blogViewService;

    @Autowired
    private HttpServletRequest request;


    public List<Blog> listAll()
    {
        return blogMapper.selectBasicBlogs();
    }

    public List<Blog> addOtherDataSize(List<Blog> blogs)
    {
        User user = userMapper.selectByAccount(JwtUtils.getAccount(request));
        Integer userId = user.getId();


        for(Blog curBlog: blogs)
        {
            Integer blogId = curBlog.getId();
            curBlog.setUsername(this.getUsernameByUserId(curBlog.getUserId()));
            curBlog.setLikeSize(blogLikeService.getLikeSizeByBlogId(blogId));
            curBlog.setFavoriteSize(blogFavoriteService.getFavoriteSizeByBlogId(blogId));
            curBlog.setCommentSize(blogCommentService.getCommentSizeByBlogId(blogId));
            curBlog.setViewSize(blogViewService.countViewsByBlogId(blogId));
            curBlog.setMyLike(blogLikeService.isMyLike(blogId, userId));
            curBlog.setMyFavorite(blogFavoriteService.isMyFavorite(blogId, userId));
            curBlog.setMyBlog(Objects.equals(userId, curBlog.getUserId()));
        }
        return blogs;
    }

    public List<Blog> filterDisplayable(List<Blog> blogs)
    {
        blogs.removeIf(curBlog -> !curBlog.getIsPublic() && !curBlog.getMyBlog());
        return blogs;
    }

    public List<Blog> getRecommendBlogs()
    {

        List<Blog> list = filterDisplayable(addOtherDataSize(listAll()));

        list.sort(new Comparator<Blog>() {
            @Override
            public int compare(Blog blog1, Blog blog2) {
                return Integer.compare(blog2.getRecommendIndex(), blog1.getRecommendIndex());
            }
        });
        return list;
    }

    public Result1 listRecommendAll()
    {
        System.out.println("按推荐排序");
        List<Blog> list = this.getRecommendBlogs();
        return Result1.ok().data("items", list);
    }

    public Result1 hotlist()
    {
        System.out.println("hotlist");
        List<Blog> list = this.getRecommendBlogs();
        if (list.size() > 6)
        {
            list = list.subList(0, 6);
        }

        return Result1.ok().data("items", list);
    }

    public Result1 listLatestAll()
    {
        System.out.println("按时间排序");
        List<Blog> list = filterDisplayable(addOtherDataSize(listAll()));
        list.sort(new Comparator<Blog>() {
            @Override
            public int compare(Blog blog1, Blog blog2) {
                LocalDateTime dateTime1 = LocalDateTime.parse(blog1.getTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime dateTime2 = LocalDateTime.parse(blog2.getTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                return dateTime2.compareTo(dateTime1);
            }
        });
        return Result1.ok().data("items", list);
    }


    public Result1 listMyBlogAll()
    {

        List<Blog> list = this.getRecommendBlogs();
        list.removeIf(curBlog -> !curBlog.getMyBlog());
        return Result1.ok().data("items", list);
    }

    public Result1 listMyLikeAll()
    {
        List<Blog> list = this.getRecommendBlogs();
        list.removeIf(curBlog -> !curBlog.getMyLike());
        return Result1.ok().data("items", list);
    }

    public Result1 listMyFavoriteAll()
    {
        List<Blog> list = this.getRecommendBlogs();
        list.removeIf(curBlog -> !curBlog.getMyFavorite());
        return Result1.ok().data("items", list);
    }

    public Result1 getPostById(Integer blogId)
    {
        User user = userMapper.selectByAccount(JwtUtils.getAccount(request));
        Integer userId = user.getId();

        Blog blog = blogMapper.selectById(blogId);
        if (blog == null || (!blog.getIsPublic() && !blog.getUserId().equals(userId)))
            return Result1.error();

        blog.setUsername(this.getUsernameByUserId(blog.getUserId()));
        blog.setLikeSize(blogLikeService.getLikeSizeByBlogId(blogId));
        blog.setFavoriteSize(blogFavoriteService.getFavoriteSizeByBlogId(blogId));
        blog.setViewSize(blogViewService.countViewsByBlogId(blogId));

        if(blogLikeService.isMyLike(blog.getId(), userId))
        {
            blog.setMyLike(true);
            blog.setMyLikeId(blogLikeService.getMyLikeId(blog.getId(), userId));
        }

        if(blogFavoriteService.isMyFavorite(blog.getId(), userId))
        {
            blog.setMyFavorite(true);
            blog.setMyFavoriteId(blogFavoriteService.getMyFavoriteId(blog.getId(), userId));
        }

        blogViewService.insert(userId, blogId);

        if(blog.getUserId().equals(userId))
            blog.setMyBlog(true);

        return Result1.ok().data("items", blog);
    }


    public Result1 insert(Blog blog)
    {
        Integer userId = JwtUtils.getId(request);

        blog.setUserId(userId);
        blog.setId(0);
        int i = blogMapper.insert(blog);
        return i > 0 ? Result1.ok() : Result1.error();
    }

    public Result1 delete(Integer blogId)
    {
        blogLikeService.deleteLikesByBlogId(blogId);
        blogCommentService.deleteCommentsByBlogId(blogId);
        blogFavoriteService.deleteFavoritesByBlogId(blogId);
        blogViewService.deleteByBlogId(blogId);
        return blogMapper.deleteById(blogId) > 0 ? Result1.ok() : Result1.error();
    }

    public Result1 updateBlogStatusByBlogId(Integer blogId)
    {
        Blog blog = blogMapper.selectById(blogId);
        blog.setIsPublic(!blog.getIsPublic());
        int i = blogMapper.updateById(blog);
        return i > 0 ? Result1.ok() : Result1.error();
    }

    public Result1 updateBlogByBlogId(Integer blogId, Blog blog)
    {
        Blog originalBlog = blogMapper.selectById(blogId);
        originalBlog.setContent(blog.getContent());
        originalBlog.setTitle(blog.getTitle());
        originalBlog.setIsPublic(blog.getIsPublic());
        int i = blogMapper.updateById(originalBlog);
        return i > 0 ? Result1.ok() : Result1.error();
    }


    public Boolean deleteAllByUserId(Integer userId)
    {
        List<Blog> blogs = blogMapper.selectByUserId(userId);
        for (Blog curBlog : blogs) {
            this.delete(curBlog.getId());
        }

        blogLikeService.deleteAllByUserId(userId);
        blogFavoriteService.deleteAllByUserId(userId);
        blogCommentService.deleteAllByUserId(userId);
        blogViewService.deleteByUserId(userId);

        return true;
    }

    public String getUsernameByUserId(Integer userId)
    {
        return userMapper.selectById(userId).getUsername();
    }

}