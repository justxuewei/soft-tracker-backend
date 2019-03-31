package com.niuxuewei.lucius.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.niuxuewei.lucius.core.exception.ExistedException;
import com.niuxuewei.lucius.core.exception.NotExistedException;
import com.niuxuewei.lucius.core.request.GitlabHttpRequest;
import com.niuxuewei.lucius.core.request.GitlabHttpRequestAuthMode;
import com.niuxuewei.lucius.entity.dto.AuthRegisterDTO;
import com.niuxuewei.lucius.entity.po.GitlabUserPO;
import com.niuxuewei.lucius.entity.po.RolePO;
import com.niuxuewei.lucius.entity.po.UserPO;
import com.niuxuewei.lucius.entity.po.UserRolePO;
import com.niuxuewei.lucius.mapper.GitlabUserPOMapper;
import com.niuxuewei.lucius.mapper.RolePOMapper;
import com.niuxuewei.lucius.mapper.UserPOMapper;
import com.niuxuewei.lucius.mapper.UserRolePOMapper;
import com.niuxuewei.lucius.service.IUserService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserPOMapper userMapper;

    @Resource
    private RolePOMapper roleMapper;

    @Resource
    private UserRolePOMapper userRoleMapper;

    @Resource
    private GitlabHttpRequest gitlabHttpRequest;

    @Resource
    private GitlabUserPOMapper gitlabUserMapper;

    @Override
    public UserPO getUserByUsername(String username) {
        return userMapper.selectFirstByUsername(username);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(AuthRegisterDTO authRegisterDTO) {
        // 将新用户插入到user表中
        UserPO userPO = insertNewUserIntoUser(authRegisterDTO);
        Integer gitlabId = createGitlabUser(authRegisterDTO);

        GitlabUserPO gitlabUserPO = new GitlabUserPO();
        gitlabUserPO.setUserId(userPO.getId());
        gitlabUserPO.setGitlabId(gitlabId);
        gitlabUserPO.setCreateDate(new Date());
        saveGitlabUserIntoDatabase(gitlabUserPO);
    }

    /**
     * 将新用户插入到user表中
     */
    private UserPO insertNewUserIntoUser(AuthRegisterDTO authRegisterDTO) {
        UserPO userPO = new UserPO();
        userPO.setUsername(authRegisterDTO.getUsername());
        userPO.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(authRegisterDTO.getPassword()));
        userPO.setEmail(authRegisterDTO.getEmail());
        userPO.setMemberSince(new Date());

        UserPO checkUser = getUserByUsername(userPO.getUsername());
        if (checkUser != null) throw new ExistedException("用户已存在");
        // 插入用户
        userMapper.insertSelective(userPO);
        Integer uid = userPO.getId();
        // 插入默认角色: 学生, role_id: 1
        if (authRegisterDTO.getRoles() == null) {
            UserRolePO userRolePO = new UserRolePO();
            userRolePO.setRoleId(1);
            userRolePO.setUserId(uid);
            userRoleMapper.insertSelective(userRolePO);
            return userPO;
        }
        // 插入角色
        List<UserRolePO> userRolePOS = new ArrayList<>();
        for (String roleString : authRegisterDTO.getRoles()) {
            RolePO rolePO = roleMapper.selectFirstByRole(roleString);
            if (rolePO == null) throw new NotExistedException("角色非法");
            Integer rid = rolePO.getId();
            // 插入到user_role中
            UserRolePO userRolePO = new UserRolePO();
            userRolePO.setRoleId(rid);
            userRolePO.setUserId(uid);
            userRolePOS.add(userRolePO);
        }
        userRoleMapper.insertListWithoutId(userRolePOS);
        return userPO;
    }

    /**
     * 去gitlab中创建一个账户
     * @return gitlabId
     */
    private Integer createGitlabUser(AuthRegisterDTO authRegisterDTO) {

        String createGitlabJsonString = gitlabHttpRequest.post(GitlabHttpRequestAuthMode.ADMIN_AUTH, "/users",
                new LinkedMultiValueMap<String, String>() {{
                    add("username", authRegisterDTO.getUsername());
                    add("name", authRegisterDTO.getUsername());
                    add("password", authRegisterDTO.getPassword());
                    add("email", authRegisterDTO.getEmail());
                }});
        JSONObject jsonObject = JSON.parseObject(createGitlabJsonString);
        return jsonObject.getInteger("id");
    }

    /**
     * 保存gitlab user信息到gitlab_user表
     */
    private void saveGitlabUserIntoDatabase(GitlabUserPO gitlabUserPO) {
        if (gitlabUserMapper.selectFirstByUserId(gitlabUserPO.getUserId()) != null) {
            throw new ExistedException("用户的gitlab账号已存在");
        }
        gitlabUserMapper.insertSelective(gitlabUserPO);
    }
}
