package com.niuxuewei.lucius.controller;

import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.core.result.ResultBuilder;
import com.niuxuewei.lucius.entity.dto.AuthRegisterDTO;
import com.niuxuewei.lucius.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private IUserService userService;

    @PostMapping("/register")
    public Result register(@Valid @RequestBody AuthRegisterDTO authRegisterDTO) {
        userService.register(authRegisterDTO);
        return ResultBuilder.SuccessResult();
    }

}
