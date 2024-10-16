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

    @GetMapping("")
    public Result get()
    {
        return dailyService.get();
    }

    @GetMapping("/list")
    public Result list()
    {
        return dailyService.list();
    }

    @GetMapping("/question/{date}")
    public Result getQuestion(@PathVariable String date)
    {
        return dailyService.getQuestion(date);
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
}


















