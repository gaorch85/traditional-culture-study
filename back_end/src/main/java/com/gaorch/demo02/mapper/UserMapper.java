package com.gaorch.demo02.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaorch.demo02.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM user WHERE account = #{account}")
    public User selectByAccount(String account);

    @Select("SELECT id FROM user WHERE account = #{account}")
    public Integer getIdByAccount(String account);

    @Select("SELECT account FROM user WHERE id = #{id}")
    public String getAccountById(Integer id);

    @Update("update user set account = #{account} where id = #{id}")
    public int updateAccountById(String account, Integer id);

    @Update("update user set username = #{username} where id = #{id}")
    public int updateUsernameById(String username, Integer id);

    @Select("select username from user where id = #{id}")
    public String selectUsername(Integer id);

    @Update("update user set points = #{points} where id = #{id}")
    public int updatePoints(Integer points, Integer id);

    @Select("select points from user where id = #{id}")
    public Integer selectPoints(Integer id);

    @Select("select count(*) from user")
    public Integer selectCount();

    @Select("SELECT COUNT(*) FROM user WHERE points < (SELECT points FROM user WHERE id = #{id})")
    public Integer selectLoser(Integer id);
}