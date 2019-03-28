package com.niuxuewei.lucius.core.shiro.realm;

import com.niuxuewei.lucius.core.shiro.token.JWTToken;
import com.niuxuewei.lucius.core.jwt.JWTUtil;
import com.niuxuewei.lucius.domain.bo.User;
import com.niuxuewei.lucius.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class LuciusRealm extends AuthorizingRealm {

    @Resource
    private IUserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有需要检测用户权限的时候才会调用此方法
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = JWTUtil.getUsername(principalCollection.toString());
        User user = userService.getUserByUsername(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole(user.getRole());
        return simpleAuthorizationInfo;
    }

    /**
     * 此方法进行用户名正确与否验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        // 检查token用户名
        String username = JWTUtil.getUsername(token);
        if (username == null) throw new AuthenticationException("token非法");
        // 检查用户
        User user = userService.getUserByUsername(username);
        if (user == null) throw new AuthenticationException("用户非法");
        // 检查密码
        if (!JWTUtil.verify(token, username, user.getPassword())) throw new AuthenticationException("用户或密码错误");
        return new SimpleAuthenticationInfo(token, token, "lucius_realm");
    }
}
