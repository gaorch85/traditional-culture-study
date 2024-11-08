package com.gaorch.demo02.controller;

import com.gaorch.demo02.entity.Blog;
import com.gaorch.demo02.entity.BlogComment;
import com.gaorch.demo02.service.BlogCommentService;
import com.gaorch.demo02.service.BlogFavoriteService;
import com.gaorch.demo02.service.BlogLikeService;
import com.gaorch.demo02.service.BlogService;
import com.gaorch.demo02.utils.Response;
import com.gaorch.demo02.utils.Result1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogLikeService blogLikeService;
    @Autowired
    private BlogFavoriteService blogFavoriteService;
    @Autowired
    private BlogCommentService blogCommentService;


    @GetMapping("/blog/list/{sortIndex}")
    public Response getAll(@PathVariable String sortIndex)
    {
        System.out.println(sortIndex);
        Result1 result1 = Result1.error();
        if("latest".equals(sortIndex))
            result1 = blogService.listLatestAll();
        if("myBlog".equals(sortIndex))
            result1 = blogService.listMyBlogAll();
        if("myLike".equals(sortIndex))
            result1 = blogService.listMyLikeAll();
        if("myFavorite".equals(sortIndex))
            result1 = blogService.listMyFavoriteAll();
        if("recommend".equals(sortIndex))
            result1 = blogService.listRecommendAll();
        return result1.isSuccess() ?
                Response.ok().setData(result1.getData()) : Response.error();
    }

    @GetMapping("/blog/{postId}")
    public Response getBlogById(@PathVariable Integer postId)
    {
        //System.out.println(postId);
        Result1 result1 = blogService.getPostById(postId);
        return result1.isSuccess() ?
                Response.ok().setData(result1.getData()) : Response.error();
    }

    @DeleteMapping("/blog/{postId}")
    public Response deleteBlogById(@PathVariable Integer postId)
    {
        Result1 result1 = blogService.delete(postId);
        return result1.isSuccess() ? Response.ok() : Response.error();
    }

    @PutMapping("/blog/update_status/{postId}")
    public Response updateBlogStatusByBlogId(@PathVariable Integer postId)
    {
        Result1 result1 = blogService.updateBlogStatusByBlogId(postId);
        return result1.isSuccess() ? Response.ok() : Response.error();
    }

    @PutMapping("/blog/{postId}")
    public Response updateBlogById(@PathVariable Integer postId, @RequestBody Blog blog)
    {
        Result1 result1 = blogService.updateBlogByBlogId(postId, blog);
        return result1.isSuccess() ? Response.ok() : Response.error();
    }

    @PostMapping("/blog")
    public Response insertBlog(@RequestBody Blog blog)
    {
        Result1 result1 = blogService.insert(blog);
        return result1.isSuccess() ? Response.ok() : Response.error();
    }

    @PostMapping("/blog/like/{postId}")
    public Response insertLike(@PathVariable Integer postId)
    {
        Result1 result1 = blogLikeService.insert(postId);
        return result1.isSuccess() ? Response.ok() : Response.error();
    }

    @DeleteMapping("/blog/like/{likeId}")
    public Response deleteLike(@PathVariable Integer likeId)
    {
        Result1 result1 = blogLikeService.delete(likeId);
        return result1.isSuccess() ? Response.ok() : Response.error();
    }

    @PostMapping("/blog/favorite/{postId}")
    public Response insertFavorite(@PathVariable Integer postId)
    {
        Result1 result1 = blogFavoriteService.insert(postId);
        return result1.isSuccess() ? Response.ok() : Response.error();
    }

    @DeleteMapping("/blog/favorite/{favoriteId}")
    public Response deleteFavorite(@PathVariable Integer favoriteId)
    {
        Result1 result1 = blogFavoriteService.delete(favoriteId);
        return result1.isSuccess() ? Response.ok() : Response.error();
    }

    @GetMapping("/blog/comment/{postId}")
    public Response getCommentsByBlogId(@PathVariable Integer postId)
    {
        System.out.println(postId);
        Result1 result1 = blogCommentService.getCommentsByBlogId(postId);
        return result1.isSuccess() ?
                Response.ok().setData(result1.getData()) : Response.error();
    }

    @PostMapping("/blog/comment/{postId}")
    public Response addComment(@PathVariable Integer postId,@RequestBody BlogComment blogComment)
    {
        System.out.println(blogComment);
        Result1 result1 = blogCommentService.insert(postId, blogComment.getContent());
        return result1.isSuccess() ? Response.ok() : Response.error();
    }

    @DeleteMapping("/blog/comment/{id}")
    public Response deleteComment(@PathVariable Integer id)
    {
        Result1 result1 = blogCommentService.delete(id);
        return result1.isSuccess() ? Response.ok() : Response.error();
    }

    @GetMapping("/blog/hotlist")
    public Response hotlist()
    {
        Result1 result1 = blogService.hotlist();
        return result1.isSuccess() ? Response.ok() : Response.error();
    }

}
