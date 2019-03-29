package com.niuxuewei.lucius.core.advice;

import com.niuxuewei.lucius.core.exception.UnauthorizedException;
import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.core.result.ResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public Result handle401(AccessDeniedException e) {
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

    /**
     * 参数异常处理
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleInvalidParam(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();

        log.error("参数异常", e);
        if (fieldError == null || fieldError.getDefaultMessage() == null) {
            return ResultBuilder.InvalidParameterResult();
        }
        return ResultBuilder.InvalidParameterResult(fieldError.getDefaultMessage());
    }

}
