package com.gaorch.demo02.service;

import com.gaorch.demo02.entity.User;
import com.gaorch.demo02.mapper.DailyMapper;
import com.gaorch.demo02.mapper.MessageMapper;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PersonalService
{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private DailyMapper dailyMapper;

    @Autowired
    private HttpServletRequest request;     //用于解析请求头中的token

    public Result getInfo()
    {
        String username = JwtUtils.getUserName(request);
        User user = userMapper.selectByUsername(username);
        return Result.ok(user);
    }

    public Result getUserId()
    {
        Integer id = JwtUtils.getId(request);
        return Result.ok(id);
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

    public Result delete() {
        Integer id = JwtUtils.getId(request);
        userMapper.deleteById(id);
        messageMapper.deleteByConversationId(id);
        dailyMapper.deleteByUserId(id);

        //linux
        File directory = new File("/home/vivy/pic/");
        //windows
        //File directory = new File("D:\\pic\\");
        String filePath = "";
        File[] matchingFiles = directory.listFiles((dir, name) -> name.startsWith(id + "_"));
        if (matchingFiles != null && matchingFiles.length > 0) {
            filePath = matchingFiles[0].getAbsolutePath();
        }
        Path ffilePath = Paths.get(filePath);
        java.io.File f = ffilePath.toFile();
        f.delete();

        return Result.ok();
    }

    public Result uploadPic(MultipartFile file) throws IOException {
        Integer userId = JwtUtils.getId(request);

        //linux
        File directory = new File("/home/vivy/pic/");
        //windows
        //File directory = new File("D:\\pic\\");

        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = "";
        File[] matchingFiles = directory.listFiles((dir, name) -> name.startsWith(userId + "_"));
        if (matchingFiles != null && matchingFiles.length > 0) {
            filePath = matchingFiles[0].getAbsolutePath();
        }
        Path ffilePath = Paths.get(filePath);
        java.io.File f = ffilePath.toFile();
        f.delete();

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

    public ResponseEntity<Resource> getPic() throws UnsupportedEncodingException {
        String userId = JwtUtils.getId(request).toString();

        //linux
        File directory = new File("/home/vivy/pic/");

        //windows
        //File directory = new File("D:\\pic\\");

        // 查找以 userId 开头的文件
        File[] matchingFiles = directory.listFiles((dir, name) -> name.startsWith(userId + "_"));

        if (matchingFiles != null && matchingFiles.length > 0) {
            // 解码文件名，处理特殊字符
            String filePath = matchingFiles[0].getAbsolutePath();
            String decodedPath = URLDecoder.decode(filePath, "UTF-8");

            Resource resource = resourceLoader.getResource("file:" + decodedPath);
            return ResponseEntity.ok(resource);
        } else {
            matchingFiles = directory.listFiles((dir, name) -> name.startsWith("default"));
            String filePath = matchingFiles[0].getAbsolutePath();
            String decodedPath = URLDecoder.decode(filePath, "UTF-8");

            Resource resource = resourceLoader.getResource("file:" + decodedPath);
            return ResponseEntity.ok(resource);
        }
    }

    public Result updateNickname(String nickname)
    {
        Integer userId = JwtUtils.getId(request);
        userMapper.updateNicknameById(nickname, userId);
        return Result.ok();
    }

    public Result getNickname()
    {
        Integer userId = JwtUtils.getId(request);
        String nickname = userMapper.selectNickname(userId);
        return Result.ok(nickname);
    }

    public Result points(Integer pnts)
    {
        Integer userId = JwtUtils.getId(request);
        Integer cur = userMapper.selectPoints(userId);
        userMapper.updatePoints(cur + pnts, userId);
        return Result.ok();
    }

    public double divideAndRound(int numerator, int denominator) {
        if (denominator == 0) {
            return 1;
        }
        BigDecimal result = new BigDecimal(numerator)
                .divide(new BigDecimal(denominator), 4, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    public Result percent()
    {
        Integer userId = JwtUtils.getId(request);
        Integer sum = userMapper.selectCount();
        Integer loser = userMapper.selectLoser(userId);
        double res = divideAndRound(loser, sum - 1);
        return Result.ok(res * 100);
    }

    public Result getPoints()
    {
        Integer userId = JwtUtils.getId(request);
        Integer res = userMapper.selectPoints(userId);
        return Result.ok(res);
    }
}
















