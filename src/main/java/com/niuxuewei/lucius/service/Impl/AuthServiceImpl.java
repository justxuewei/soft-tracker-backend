package com.niuxuewei.lucius.service.Impl;

import com.niuxuewei.lucius.entity.po.User;
import com.niuxuewei.lucius.mapper.UserMapper;
import com.niuxuewei.lucius.service.IAuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class AuthServiceImpl implements IAuthService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void register(User user) {
        userMapper.insertSelective(user);
    }

    @Override
    public String login(String username, String password) {
        return null;
    }
}
