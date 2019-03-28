package com.niuxuewei.lucius.service.Impl;

import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.core.result.ResultBuilder;
import com.niuxuewei.lucius.domain.bo.User;
import com.niuxuewei.lucius.mapper.UserMapper;
import com.niuxuewei.lucius.service.IUserService;

import javax.annotation.Resource;

public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectFirstByUsername(username);
    }

}
