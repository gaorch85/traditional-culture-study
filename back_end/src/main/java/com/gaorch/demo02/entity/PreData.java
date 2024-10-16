package com.gaorch.demo02.entity;

import lombok.Data;

import java.util.List;

@Data
public class PreData
{
    private List<Message> lastMessages;
    private String backIntro;
}
