package com.niuxuewei.lucius.core.result;

import com.niuxuewei.lucius.core.enumeration.ResultCode;

public class ResultBuilder {

    private static final String DEFAULT_SUCCESS_MESSAGE = "成功";

    public static Result SuccessResult() {
        return new Result()
                .setCodeAndMessage(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result SuccessResult(Object data) {
        return new Result()
                .setCodeAndMessage(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result FailResult(String message) {
        return new Result()
                .setCodeAndMessage(ResultCode.FAIL)
                .setMessage(message);
    }

    public static Result FailResult(String message, Object data) {
        return new Result()
                .setCodeAndMessage(ResultCode.FAIL)
                .setMessage(message)
                .setData(data);
    }

    public static Result UnauthorizedResult() {
        return new Result().setCodeAndMessage(ResultCode.UNAUTHORIZED);
    }

    public static Result InvalidParameterResult() {
        return new Result().setCodeAndMessage(ResultCode.INVALID_PARAM);
    }

    public static Result InvalidParameterResult(String msg) {
        return new Result().setCodeAndMessage(ResultCode.INVALID_PARAM).setMessage(msg);
    }

    public static Result InternalServerErrorResult() {
        return new Result().setCodeAndMessage(ResultCode.INTERNAL_SERVER_ERROR);
    }

    public static Result InternalServerErrorResult(String message) {
        return new Result().setCodeAndMessage(ResultCode.INTERNAL_SERVER_ERROR)
                .setMessage(message);
    }

    public static Result NotFoundResult(String message) {
        return new Result().setCodeAndMessage(ResultCode.NOT_FOUND).setMessage(message);
    }

}
