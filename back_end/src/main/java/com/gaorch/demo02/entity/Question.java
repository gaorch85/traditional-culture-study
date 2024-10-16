package com.gaorch.demo02.entity;

import lombok.Data;

@Data
public class Question
{
    private Integer id;
    private Integer level;
    private String content;
    private String date;
}
