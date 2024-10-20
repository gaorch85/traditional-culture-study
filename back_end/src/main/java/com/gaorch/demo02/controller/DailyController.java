package com.gaorch.demo02.controller;

import com.gaorch.demo02.entity.Answer;
import com.gaorch.demo02.service.DailyService;
import com.gaorch.demo02.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/daily")
public class DailyController
{
    @Autowired
    private DailyService dailyService;

    @GetMapping("/{date}")
    public Result get(@PathVariable String date)
    {
        return dailyService.get(date);
    }

    @GetMapping("/list")
    public Result list()
    {
        return dailyService.list();
    }

    @GetMapping("/answer/{questionId}")
    public Result getAnswer(@PathVariable Integer questionId)
    {
        return dailyService.getAnswer(questionId);
    }

    @PostMapping("/answer")
    public Result answer(@RequestBody Answer answer)
    {
        return dailyService.answer(answer);
    }

    @GetMapping("/cnt/{questionId}")
    public Result cnt(@PathVariable Integer questionId)
    {
        return dailyService.cnt(questionId);
    }

    @GetMapping("/month")
    public Result month()
    {
        return dailyService.month();
    }

    @GetMapping("/continue")
    public Result contin()
    {
        return dailyService.contin();
    }
}


















