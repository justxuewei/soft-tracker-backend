package com.niuxuewei.lucius.service;

import com.niuxuewei.lucius.entity.dto.AddSSHKeyDTO;
import com.niuxuewei.lucius.entity.dto.AuthRegisterDTO;
import com.niuxuewei.lucius.entity.po.UserPO;
import com.niuxuewei.lucius.entity.vo.AddSSHKeyVO;
import com.niuxuewei.lucius.entity.vo.GetSSHKeysVO;
import com.niuxuewei.lucius.entity.vo.GetUserInfoVO;
import com.niuxuewei.lucius.entity.vo.SearchUserVO;

import java.util.List;

public interface IUserService {

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 如果找到该用户返回User类
     */
    UserPO getUserByUsernameOrEmail(String username, String email);

    GetUserInfoVO getUserInfo();

    /**
     * 用户注册，校验用户名是否重复
     */
    void register(AuthRegisterDTO authRegisterDTO);

    /**
     * ssh key 相关操作
     */
    List<GetSSHKeysVO> getSSHKeys();

    AddSSHKeyVO addSSHKey(AddSSHKeyDTO addSSHKeyDTO) throws Exception;

    void deleteSSHKey(String keyId);

    List<SearchUserVO> searchStudent(String username, String email);
}
