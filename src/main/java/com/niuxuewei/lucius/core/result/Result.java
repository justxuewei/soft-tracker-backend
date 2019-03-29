package com.niuxuewei.lucius.core.result;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * 统一json返回格式
 */
@Slf4j
public class Result {

    private int code;
    private String message;
    private Object data;

    public Result() {}

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public Result(ResultCode resultCode, Object data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public Result setCodeAndMessage(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        return this;
    }

    public int getCode() {
        return code;
    }

    public Result setCodeAndMessage(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
