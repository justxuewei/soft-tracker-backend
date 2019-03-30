package com.niuxuewei.lucius.core.utils;

import com.niuxuewei.lucius.core.exception.UnauthorizedException;
import com.niuxuewei.lucius.entity.po.User;
import com.niuxuewei.lucius.entity.security.UserDetailsImpl;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /**
     * 获取已经登录的用户
     */
    public static User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getDetails();
            return userDetails.getUser();
        }
        throw new UnauthorizedException();
    }

}
