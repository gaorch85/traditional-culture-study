package com.gaorch.demo02.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result1
{
    private boolean success;

    private String message;

    private Map<String, Object> data = new HashMap<>();

    private Result1() { }

    public Result1 data(String key, Object value)
    {
        this.data.put(key, value);
        return this;
    }

    public static Result1 ok()
    {
        Result1 result1 = new Result1();
        result1.setSuccess(true);
        result1.setMessage("SUCCESS!");
        return result1;
    }

    public static Result1 error()
    {
        Result1 result1 = new Result1();
        result1.setSuccess(false);
        result1.setMessage("ERROR!");
        return result1;
    }



}
