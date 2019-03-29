package com.niuxuewei.lucius.service.Impl;

import com.niuxuewei.lucius.entity.po.Role;
import com.niuxuewei.lucius.entity.po.User;
import com.niuxuewei.lucius.entity.po.UserRole;
import com.niuxuewei.lucius.entity.security.UserDetailsImpl;
import com.niuxuewei.lucius.mapper.RoleMapper;
import com.niuxuewei.lucius.mapper.UserMapper;
import com.niuxuewei.lucius.mapper.UserRoleMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 根据用户名寻找用户
     */
    private User selectUserByUsername(String username) {
        return userMapper.selectFirstByUsername(username);
    }

    /**
     * 根据用户ID获取用户角色
     */
    private Collection<? extends GrantedAuthority> getUserAuthorities(Integer uid) {
        List<UserRole> userRoles = userRoleMapper.selectByUserId(uid);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole userRole: userRoles) {
            Role role = roleMapper.selectByPrimaryKey(userRole.getRoleId());
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = selectUserByUsername(s);
        if (user == null) throw new UsernameNotFoundException("用户未找到");
        Collection<? extends GrantedAuthority> authorities = getUserAuthorities(user.getId());
        return new UserDetailsImpl(user, authorities);
    }
}
