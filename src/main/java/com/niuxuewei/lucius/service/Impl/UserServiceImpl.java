package com.niuxuewei.lucius.service.Impl;

import com.niuxuewei.lucius.core.exception.ExistedException;
import com.niuxuewei.lucius.core.exception.NotExistedException;
import com.niuxuewei.lucius.entity.po.Role;
import com.niuxuewei.lucius.entity.po.User;
import com.niuxuewei.lucius.entity.po.UserRole;
import com.niuxuewei.lucius.mapper.RoleMapper;
import com.niuxuewei.lucius.mapper.UserMapper;
import com.niuxuewei.lucius.mapper.UserRoleMapper;
import com.niuxuewei.lucius.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectFirstByUsername(username);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User register(User user, List<String> roles) {
        User checkUser = getUserByUsername(user.getUsername());
        if (checkUser != null) throw new ExistedException("用户已存在");
        // 插入用户
        userMapper.insertSelective(user);
        Integer uid = user.getId();
        // 插入默认角色: 学生, role_id: 1
        if (roles == null) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(1);
            userRole.setUserId(uid);
            userRoleMapper.insertSelective(userRole);
            return user;
        }
        // 插入角色
        List<UserRole> userRoles = new ArrayList<>();
        for (String roleString: roles) {
            Role role = roleMapper.selectFirstByRole(roleString);
            if (role == null) throw new NotExistedException("角色非法");
            Integer rid = role.getId();
            // 插入到user_role中
            UserRole userRole = new UserRole();
            userRole.setRoleId(rid);
            userRole.setUserId(uid);
            userRoles.add(userRole);
        }
        userRoleMapper.insertListWithoutId(userRoles);
        return user;
    }
}
