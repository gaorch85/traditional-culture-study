package com.gaorch.demo02.entity;

import lombok.Data;

@Data
public class Answer
{
    private Integer id;
    private Integer userId;
    private Integer questionId;
    private String content;
    private Integer score;
    private String reason;
    private String date;
}
