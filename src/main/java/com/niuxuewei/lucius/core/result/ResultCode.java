package com.niuxuewei.lucius.core.result;

import lombok.Getter;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {

    SUCCESS(200, "成功"),  //成功
    FAIL(400, "访问失败"),  //失败
    UNAUTHORIZED(401, "授权失败"),  //未认证（签名错误）
    NOT_FOUND(404, "此接口不存在"),   //接口不存在
    INTERNAL_SERVER_ERROR(500, "系统繁忙，请稍后再试"),   //服务器内部错误
    INVALID_PARAM(10000, "参数错误");

    @Getter
    private int code;
    @Getter
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
