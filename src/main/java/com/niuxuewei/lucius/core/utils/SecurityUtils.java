package com.niuxuewei.lucius.core.utils;

import com.niuxuewei.lucius.core.exception.UnauthorizedException;
import com.niuxuewei.lucius.entity.po.UserPO;
import com.niuxuewei.lucius.mapper.UserPOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityUtils {

    private static ThreadLocal<UserPO> LOCAL = new ThreadLocal<>();

    private static UserPOMapper userMapper;

    @Autowired
    public SecurityUtils(UserPOMapper userMapper) {
        SecurityUtils.userMapper = userMapper;
    }

    /**
     * 获取已经登录的用户
     */
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        throw new UnauthorizedException();
    }

    /**
     * 获取目前登录的用户
     * 首先先根据用户名从数据库中获取用户，存入LOCAL中
     */
    public static UserPO getUser() {
        String username = getUsername();
        UserPO userPO = LOCAL.get();
        // LOCAL中的用户不存在或者不匹配
        if (userPO == null || !userPO.getUsername().equals(username)) {
            log.debug("LOCAL中的用户不存在或者不匹配");
            userPO = userMapper.selectFirstByUsername(username);
            LOCAL.set(userPO);
        }
        return userPO;
    }

    public static Integer getUserId() {
        return getUser().getId();
    }

}
