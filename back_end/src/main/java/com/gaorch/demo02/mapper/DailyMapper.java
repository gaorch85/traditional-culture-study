package com.gaorch.demo02.mapper;

import com.gaorch.demo02.entity.Answer;
import com.gaorch.demo02.entity.Question;
import com.gaorch.demo02.entity.Theme;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DailyMapper
{
    @Select("select * from theme where date = #{date}")
    public Theme selectTheme(String date);

    @Select("select * from theme")
    public List<Theme> selectAllTheme();

    @Select("select * from question where date = #{date}")
    public List<Question> selectQuestionByDate(String date);

    @Select("select * from question where id = #{id}")
    public Question selectQuestionById(Integer id);

    @Select("select * from answer where userId = #{userId} and questionId = #{questionId}")
    public List<Answer> selectAnswerByQuestionId(Integer userId, Integer questionId);

    @Select("select * from answer where userId = #{userId} and date = #{date}")
    public List<Answer> selectAnswerByDate(Integer userId, String date);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into answer (userId,questionId,content,score,reason) values (#{userId},#{questionId},#{content},#{score},#{reason})")
    public int insertAnswer(Answer answer);

    @Insert("insert into theme (content, intro) values (#{content}, #{intro})")
    public int insertTheme(String content, String intro);

    @Insert("insert into question (level, content) values (#{level},#{content})")
    public int insertQuestion(Integer level, String content);

    @Select("select count(*) from answer where questionId = #{questionId}")
    public Integer selectCount(Integer questionId);

    @Delete("delete from answer where userId = #{userId}")
    public int deleteByUserId(Integer userId);
}















