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
import java.io.UnsupportedEncodingException;

@RestController
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @GetMapping("/getInfo")
    public Result getInfo() {
        return personalService.getInfo();
    }

    @GetMapping("/getUserId")
    public Result getUserId()
    {
        return personalService.getUserId();
    }

    @PutMapping("/changePassword")
    public Result changePassword(@RequestBody User user) {
        return personalService.changePassword(user);
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
    public ResponseEntity<Resource> getPic() throws UnsupportedEncodingException {
        return personalService.getPic();
    }

    @PutMapping("/username/{username}")
    public Result updateUsername(@PathVariable String username)
    {
        return personalService.updateUsername(username);
    }

    @GetMapping("/username")
    public Result getUsername()
    {
        return personalService.getUsername();
    }

    @PostMapping("/points/{pnts}")
    public Result points(@PathVariable Integer pnts)
    {
        return personalService.points(pnts);
    }

    @GetMapping("/percent")
    public Result percent()
    {
        return personalService.percent();
    }

    @GetMapping("/points")
    public Result getPoints()
    {
        return personalService.getPoints();
    }
}












