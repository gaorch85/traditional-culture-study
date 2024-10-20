package com.gaorch.demo02.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String salt;
    private Integer score;
}
