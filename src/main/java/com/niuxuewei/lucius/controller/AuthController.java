package com.niuxuewei.lucius.controller;

import com.alibaba.fastjson.JSONObject;
import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.core.validator.JSONValidator;
import com.niuxuewei.lucius.entity.domain.User;
import com.niuxuewei.lucius.service.IAuthService;
import com.niuxuewei.lucius.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private IUserService userService;

    @Resource
    private IAuthService authService;

    @PostMapping("/login")
    public Result login(@RequestBody JSONObject jsonObject) {
        return null;
    }

    @PostMapping("/register")
    public Result register(@RequestBody JSONObject jsonObject) {
        JSONValidator validator = new JSONValidator(jsonObject);
        String username = validator.validate("username");
        String password = validator.validate("password");
        String email = validator.validate("email");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setEmail(email);
        return null;
    }

}
