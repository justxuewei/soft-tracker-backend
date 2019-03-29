package com.niuxuewei.lucius.service;

import com.niuxuewei.lucius.entity.po.User;

public interface IAuthService {

    /**
     * 注册
     *
     * @param user 用户，注意这里的用户将不再验证是否username重复
     */
    void register(User user);

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回用户token
     */
    String login(String username, String password);

}
