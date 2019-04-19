package com.niuxuewei.lucius.entity.vo;

import lombok.Data;

@Data
public class GetProjectDetailsMembersVO {

    // 用户ID
    private Integer id;

    // 用户名称
    private String realname;

    // 用户名
    private String username;

    // 用户邮箱
    private String email;

    // 项目角色
    private String projectRole;

}
