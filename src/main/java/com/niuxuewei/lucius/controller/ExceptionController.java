package com.niuxuewei.lucius.controller;

import com.niuxuewei.lucius.core.exception.UnauthorizedException;
import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.core.result.ResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ExceptionController {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public Result handle401(ShiroException e) {
        log.error("授权失败", e);
        return ResultBuilder.UnauthorizedResult();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public Result handle401(UnauthorizedException e) {
        log.error("授权失败", e);
        return ResultBuilder.UnauthorizedResult();
    }

    /**
     * springboot无法处理filter抛出的异常
     * 通过重定向来自定义抛出异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @RequestMapping("/401")
    public void handle401() {
        throw new UnauthorizedException();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Result handleGlobalException(Exception e) {
        log.error("捕获全局异常", e);
        return ResultBuilder.FailResult(e.getMessage());
    }

}
