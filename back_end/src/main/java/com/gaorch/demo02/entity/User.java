package com.gaorch.demo02.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String account;
    private String password;
    private String username;
    private String salt;
    private Integer points;
}
