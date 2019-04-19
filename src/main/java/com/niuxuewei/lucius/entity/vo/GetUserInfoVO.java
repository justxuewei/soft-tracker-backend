package com.niuxuewei.lucius.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class GetUserInfoVO {

    private Integer id;

    private String username;

    private String realname;

    private String email;

    private List<String> roles;

}
