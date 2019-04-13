package com.niuxuewei.lucius.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class SearchUserVO {

    private Integer id;

    private String username;

    private String email;

    private List<String> roles;

}
