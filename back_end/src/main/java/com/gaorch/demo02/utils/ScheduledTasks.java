package com.gaorch.demo02.utils;

import com.gaorch.demo02.mapper.DailyMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private DailyMapper dailyMapper;

    @Autowired
    private HttpUtils httpUtils;

    @Data
    private static class Da
    {
        private String theme;
        private String introduction;
        private List<String> question;
    }

    @Scheduled(cron = "0 1 0 * * ?") // 每天零点一分执行
    public void executeTask() {
        // 这里放置您需要执行的代码
        Da da = httpUtils.PostRequest("http://localhost:8080/get_info", null, Da.class);
        dailyMapper.insertTheme(da.getTheme(), da.getIntroduction());
        dailyMapper.insertQuestion(1, da.getQuestion().get(0));
        dailyMapper.insertQuestion(2, da.getQuestion().get(1));
        dailyMapper.insertQuestion(3, da.getQuestion().get(2));
    }
}
