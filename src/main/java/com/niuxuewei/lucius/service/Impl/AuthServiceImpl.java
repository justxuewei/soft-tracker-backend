package com.niuxuewei.lucius.service.Impl;

import com.niuxuewei.lucius.entity.domain.User;
import com.niuxuewei.lucius.service.IAuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements IAuthService {

    @Override
    public Integer register(User user) {
        return null;
    }

    @Override
    public String login(String username, String password) {
        return null;
    }
}
