package com.gaorch.demo02.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Comment {
    private Integer id;
    private Integer userId;
    private Integer blogId;
    private String content;
    private String createdAt;
    @TableField(exist = false)
    private String username;
    private String region;
    @TableField(exist = false)
    private Boolean myComment;

    public void setMyComment(Integer userId)
    {
        this.myComment = userId.equals(this.getUserId());
    }
}