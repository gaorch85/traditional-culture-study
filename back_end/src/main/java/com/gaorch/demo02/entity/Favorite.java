package com.gaorch.demo02.entity;

import lombok.Data;

@Data
public class Favorite {
    private Integer id;
    private Integer userId;
    private Integer blogId;
    private String createdAt;
}
