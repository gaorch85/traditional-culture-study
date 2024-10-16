package com.gaorch.demo02.controller;

import com.gaorch.demo02.entity.User;
import com.gaorch.demo02.service.PersonalService;
import com.gaorch.demo02.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.File;

@RestController
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @GetMapping("/getInfo")
    public Result getInfo() {
        return personalService.getInfo();
    }

    @PutMapping("/changePassword")
    public Result changePassword(@RequestBody User user) {
        return personalService.changePassword(user);
    }

    @PutMapping("/changeUsername/{username}")
    public Result changeUsername(@PathVariable String username) {
       return personalService.updateUsername(username);
    }

    @DeleteMapping("/user")
    public Result delete()
    {
        return personalService.delete();
    }

    @PostMapping("/pic")
    public Result uploadPic(@RequestParam("file") MultipartFile file) throws IOException {
        return personalService.uploadPic(file);
    }

    @GetMapping("/pic")
    public ResponseEntity<Resource> getPic()
    {
        return personalService.getPic();
    }

    @PutMapping("/nickname")
    public Result updateNickname(String nickname)
    {
        return personalService.updateNickname(nickname);
    }
}
