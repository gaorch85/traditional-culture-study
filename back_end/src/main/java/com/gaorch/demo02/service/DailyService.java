package com.gaorch.demo02.service;

import com.gaorch.demo02.entity.Answer;
import com.gaorch.demo02.entity.Question;
import com.gaorch.demo02.entity.Theme;
import com.gaorch.demo02.mapper.DailyMapper;
import com.gaorch.demo02.utils.HttpUtils;
import com.gaorch.demo02.utils.JwtUtils;
import com.gaorch.demo02.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DailyService
{
    @Autowired
    private DailyMapper dailyMapper;

    @Autowired
    private HttpServletRequest request;

    @Data
    private class Dataa
    {
        private Theme theme;
        private List<Question> questions;
    };

    public Result get()
    {
        Theme theme = dailyMapper.selectTheme();
        List<Question> questions = dailyMapper.selectQuestion();

        Dataa dataa = new Dataa();
        dataa.setQuestions(questions);
        dataa.setTheme(theme);

        return Result.ok(dataa);
    }

    public Result list()
    {
        List<Theme> themes = dailyMapper.selectAllTheme();
        return Result.ok(themes);
    }

    public Result getQuestion(String date)
    {
        List<Question> questions = dailyMapper.selectQuestionByDate(date);
        return Result.ok(questions);
    }

    public Result getAnswer(Integer questionId)
    {
        Integer userId = JwtUtils.getId(request);
        List<Answer> answers = dailyMapper.selectAnswerByQuestionId(userId, questionId);
        return Result.ok(answers);
    }


    @Data
    private class Daata
    {
        private String question;
        private String answer;
    }

    @Data
    private class Daa
    {
        private Integer score;
        private String reason;
    }

    public Result answer(Answer answer)
    {
        Integer userId = JwtUtils.getId(request);
        answer.setUserId(userId);
        answer.setId(0);

        Question question = dailyMapper.selectQuestionById(answer.getQuestionId());
        Daata daata = new Daata();
        daata.setQuestion(question.getContent());
        daata.setAnswer(answer.getContent());

        Daa daa = HttpUtils.PostRequest("localhost:80/get_score", daata, Daa.class);
        answer.setScore(daa.getScore());
        answer.setReason(daa.getReason());
        dailyMapper.insertAnswer(answer);

        return Result.ok(answer);
    }

    public Result cnt(Integer questionId)
    {
        Integer cnt = dailyMapper.selectCount(questionId);
        return Result.ok(cnt);
    }

    public static String[] getCurrentMonthDates() {
        // 获取本月第一天
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        // 获取今天的日期
        LocalDate today = LocalDate.now();

        // 创建一个列表来存储日期字符串
        List<String> dateList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d"); // 使用 "yyyy-MM-d" 以处理单日

        // 从本月第一天到今天
        for (LocalDate date = firstDayOfMonth; !date.isAfter(today); date = date.plusDays(1)) {
            dateList.add(date.format(formatter));
        }

        return dateList.toArray(new String[0]); // 转换为数组
    }

    public Result month()
    {
        Integer userId = JwtUtils.getId(request);
        String[] dates = getCurrentMonthDates();
        Integer[] res = new Integer[31];

        for(int i = 0; i < dates.length; i++)
        {
            List<Answer> answers = dailyMapper.selectAnswerByDate(userId, dates[i]);
            if(answers.isEmpty())
            {
                res[i]=0;
            }
            else
            {
                while(!answers.isEmpty() && answers.get(answers.size()-1).getScore() < 60)
                {
                    answers.remove(answers.size()-1);
                }
                if(answers.isEmpty())
                {
                    res[i] = 0;
                }
                else
                {
                    Answer answer = answers.get(answers.size()-1);
                    Integer questionId = answer.getQuestionId();
                    Question question = dailyMapper.selectQuestionById(questionId);
                    res[i] = question.getLevel();
                }
            }
        }

        return Result.ok(res);
    }
}






















