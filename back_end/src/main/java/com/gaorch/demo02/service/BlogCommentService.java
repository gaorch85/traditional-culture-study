package com.gaorch.demo02.service;

import com.gaorch.demo02.entity.Comment;
import com.gaorch.demo02.entity.User;
import com.gaorch.demo02.mapper.BlogCommentMapper;
import com.gaorch.demo02.mapper.UserMapper;
import com.gaorch.demo02.utils.JwtUtils;
import com.gaorch.demo02.utils.Result1;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogCommentService {

    @Autowired
    private BlogCommentMapper blogCommentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpServletRequest request;

    public Result1 getCommentsByBlogId(Integer blogId)
    {
        List<Comment> comments = blogCommentMapper.selectByBlogId(blogId);

        User user = userMapper.selectByAccount(JwtUtils.getAccount(request));
        Integer userId = user.getId();

        for(Comment curComment: comments)
        {
            User curUser = userMapper.selectById(curComment.getUserId());
            curComment.setUsername(curUser.getUsername());
            curComment.setMyComment(userId);
        }
        return Result1.ok().data("items",comments);
    }

    public Boolean deleteCommentsByBlogId(Integer blogId)
    {
        return blogCommentMapper.deleteCommentsByBlogId(blogId);
    }

    public Boolean deleteAllByUserId(Integer userId)
    {
        return blogCommentMapper.deleteAllByUserId(userId);
    }

    public Integer getCommentSizeByBlogId(Integer blogId)
    {
        return blogCommentMapper.getCommentSizeByBlogId(blogId);
    }

    public Result1 insert(Integer postId, String comment)
    {
        String ipAddress = request.getRemoteAddr();


        User user = userMapper.selectByAccount(JwtUtils.getAccount(request));
        Integer userId = user.getId();

        Comment blogComment = new Comment();
        blogComment.setUserId(userId);
        blogComment.setBlogId(postId);
        blogComment.setId(0);
        blogComment.setContent(comment);

        blogComment.setRegion("中国 天津市");


        int i = blogCommentMapper.insert(blogComment);
        return i > 0 ? Result1.ok() : Result1.error();
    }

    public Result1 delete(Integer id)
    {
        int i = blogCommentMapper.deleteById(id);
        return i > 0 ? Result1.ok() : Result1.error();
    }
}
