package com.niuxuewei.lucius.controller;

import com.alibaba.fastjson.JSONObject;
import com.niuxuewei.lucius.core.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public Result login(@RequestBody JSONObject jsonObject) {
        return null;
    }

}
