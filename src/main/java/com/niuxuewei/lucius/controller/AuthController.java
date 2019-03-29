package com.niuxuewei.lucius.controller;

import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.core.result.ResultBuilder;
import com.niuxuewei.lucius.entity.dto.AuthLoginDTO;
import com.niuxuewei.lucius.entity.dto.AuthRegisterDTO;
import com.niuxuewei.lucius.entity.vo.AuthLoginVO;
import com.niuxuewei.lucius.service.IAuthService;
import com.niuxuewei.lucius.service.IUserService;
import org.dozer.Mapper;
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
    private Mapper dozerMapper;

    @Resource
    private IUserService userService;

    @Resource
    private IAuthService authService;

    @PostMapping("/login")
    public Result login(@Valid @RequestBody AuthLoginDTO authLoginDTO) {
        String jwtToken = authService.login(authLoginDTO.getUsername(), authLoginDTO.getPassword());
        AuthLoginVO authLoginVO = new AuthLoginVO();
        authLoginVO.setToken(jwtToken);
        return ResultBuilder.SuccessResult(jwtToken);
    }

    @PostMapping("/register")
    public Result register(@Valid @RequestBody AuthRegisterDTO authRegisterDTO) {
        return null;
    }

}
