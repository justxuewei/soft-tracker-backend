package com.niuxuewei.lucius.controller;

import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.core.result.ResultBuilder;
import com.niuxuewei.lucius.entity.dto.AuthRegisterDTO;
import com.niuxuewei.lucius.entity.po.User;
import com.niuxuewei.lucius.service.IUserService;
import org.dozer.Mapper;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private IUserService userService;

    @PostMapping("/register")
    public Result register(@Valid @RequestBody AuthRegisterDTO authRegisterDTO) {

        User user = new User();
        user.setUsername(authRegisterDTO.getUsername());
        user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(authRegisterDTO.getPassword()));
        user.setEmail(authRegisterDTO.getEmail());
        user.setMemberSince(new Date());

        user = userService.register(user, authRegisterDTO.getRoles());

        return ResultBuilder.SuccessResult();
    }

}
