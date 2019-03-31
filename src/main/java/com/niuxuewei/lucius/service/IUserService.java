package com.niuxuewei.lucius.service;

import com.niuxuewei.lucius.entity.dto.AuthRegisterDTO;
import com.niuxuewei.lucius.entity.po.UserPO;

import java.util.List;

public interface IUserService {

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 如果找到该用户返回User类
     */
    UserPO getUserByUsername(String username);

    /**
     * 用户注册，校验用户名是否重复
     */
    void register(AuthRegisterDTO authRegisterDTO);

}
