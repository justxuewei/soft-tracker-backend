package com.niuxuewei.lucius.service.Impl;

import com.niuxuewei.lucius.entity.domain.User;
import com.niuxuewei.lucius.mapper.UserMapper;
import com.niuxuewei.lucius.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectFirstByUsername(username);
    }

}
