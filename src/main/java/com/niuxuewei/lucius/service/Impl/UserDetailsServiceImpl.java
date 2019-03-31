package com.niuxuewei.lucius.service.Impl;

import com.niuxuewei.lucius.entity.po.RolePO;
import com.niuxuewei.lucius.entity.po.UserPO;
import com.niuxuewei.lucius.entity.po.UserRolePO;
import com.niuxuewei.lucius.entity.security.UserDetailsImpl;
import com.niuxuewei.lucius.mapper.RolePOMapper;
import com.niuxuewei.lucius.mapper.UserPOMapper;
import com.niuxuewei.lucius.mapper.UserRolePOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserPOMapper userMapper;

    @Resource
    private RolePOMapper roleMapper;

    @Resource
    private UserRolePOMapper userRoleMapper;

    /**
     * 根据用户名寻找用户
     */
    private UserPO selectUserByUsername(String username) {
        return userMapper.selectFirstByUsername(username);
    }

    /**
     * 根据用户ID获取用户角色
     */
    private Collection<? extends GrantedAuthority> getUserAuthorities(Integer uid) {
        List<UserRolePO> userRolePOS = userRoleMapper.selectByUserId(uid);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRolePO userRolePO : userRolePOS) {
            RolePO rolePO = roleMapper.selectByPrimaryKey(userRolePO.getRoleId());
            authorities.add(new SimpleGrantedAuthority(rolePO.getRole()));
        }
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserPO userPO = selectUserByUsername(username);
        if (userPO == null) throw new UsernameNotFoundException("用户未找到");
        Collection<? extends GrantedAuthority> authorities = getUserAuthorities(userPO.getId());
        return new UserDetailsImpl(userPO, authorities);
    }

}
