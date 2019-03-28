package com.niuxuewei.lucius.service;

import com.niuxuewei.lucius.entity.domain.User;

public interface IUserService {

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 如果找到该用户返回User类
     */
    User getUserByUsername(String username);

}
