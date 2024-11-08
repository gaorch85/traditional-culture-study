package com.gaorch.demo02.service;

import com.gaorch.demo02.entity.User;
import com.gaorch.demo02.mapper.UserMapper;
import com.gaorch.demo02.utils.JwtUtils;
import com.gaorch.demo02.utils.PasswordUtils;
import com.gaorch.demo02.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService
{
    @Autowired
    private UserMapper userMapper;

    public Result login(User user)
    {
        User selectUser = userMapper.selectByAccount(user.getAccount());
        String salt = selectUser.getSalt();
        String curHashPassword = PasswordUtils.hashPassword(user.getPassword(), salt);
        if(curHashPassword.equals(selectUser.getPassword()))
        {
            String token = JwtUtils.generateToken(user.getAccount());
            return Result.ok(token);
        }
        return Result.error();
    }

    public Result signup(User user)
    {
        user.setSalt(PasswordUtils.generateSalt());
        user.setPassword(PasswordUtils.hashPassword(user.getPassword(), user.getSalt()));
        user.setId(0);
        userMapper.insert(user);

        String token = JwtUtils.generateToken(user.getAccount());
        return Result.ok(token);

    }


}
