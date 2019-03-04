package com.niuxuewei.lucius.service.impl;

import com.niuxuewei.lucius.entity.User;
import com.niuxuewei.lucius.mapper.UserMapper;
import com.niuxuewei.lucius.service.ILoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LoginServiceImpl implements ILoginService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

}
