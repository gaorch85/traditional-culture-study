package com.gaorch.demo02.service;

import com.gaorch.demo02.entity.User;
import com.gaorch.demo02.mapper.UserMapper;
import com.gaorch.demo02.utils.JwtUtils;
import com.gaorch.demo02.utils.PasswordUtils;
import com.gaorch.demo02.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class PersonalService
{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpServletRequest request;     //用于解析请求头中的token

    public Result getInfo()
    {
        String username = JwtUtils.getUserName(request);
        User user = userMapper.selectByUsername(username);
        return Result.ok(user);
    }

    public Result changePassword(User user)
    {
        String username = JwtUtils.getUserName(request);
        String password = user.getPassword();
        User selectUser = userMapper.selectByUsername(username);
        selectUser.setSalt(PasswordUtils.generateSalt());
        selectUser.setPassword(PasswordUtils.hashPassword(password, selectUser.getSalt()));
        userMapper.updateById(selectUser);
        return Result.ok();
    }

    public Result updateUsername(String username)
    {
        Integer id = JwtUtils.getId(request);
        userMapper.updateUsernameById(username, id);
        return Result.ok();
    }

    public Result delete()
    {
        Integer id = JwtUtils.getId(request);
        userMapper.deleteById(id);
        return Result.ok();
    }

    public Result uploadPic(MultipartFile file) throws IOException {
        Integer userId = JwtUtils.getId(request);

        //windows
        //java.io.File directory = new java.io.File("D:\\pic\\");

        //linux
        java.io.File directory = new java.io.File("/root/pic/");

        if (!directory.exists()) {
            directory.mkdirs();
        }
        String file_name = file.getOriginalFilename();
        file_name = URLDecoder.decode(file_name, "UTF-8");

        File destinationFile = new File(directory, userId.toString() + "_" + file_name);
        file.transferTo(destinationFile);
        return Result.ok();
    }

    private final ResourceLoader resourceLoader;

    public PersonalService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public ResponseEntity<Resource> getPic()
    {
        String userId = JwtUtils.getId(request).toString();

        //linux
        File directory = new File("/root/pic/");

        //windows
        //File directory = new File("D:\\pic\\");

        // 查找以 userId 开头的文件
        File[] matchingFiles = directory.listFiles((dir, name) -> name.startsWith(userId + "_"));

        if (matchingFiles != null && matchingFiles.length > 0) {
            try {
                // 解码文件名，处理特殊字符
                String filePath = matchingFiles[0].getAbsolutePath();
                String decodedPath = URLDecoder.decode(filePath, "UTF-8");

                Resource resource = resourceLoader.getResource("file:" + decodedPath);
                return ResponseEntity.ok(resource);
            } catch (UnsupportedEncodingException e) {
                return ResponseEntity.internalServerError().build();
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build(); // 处理其他异常
            }
        } else {
            return ResponseEntity.notFound().build(); // 找不到头像文件
        }
    }

    public Result updateNickname(String nickname)
    {
        Integer userId = JwtUtils.getId(request);
        userMapper.updateNicknameById(nickname, userId);
        return Result.ok();
    }
}
