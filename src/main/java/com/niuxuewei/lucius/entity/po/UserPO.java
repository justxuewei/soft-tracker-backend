package com.niuxuewei.lucius.entity.po;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UserPO implements Serializable {

    private static final long serialVersionUID = -6231109738362652842L;

    private Integer id;

    private String username;

    private String realname;

    private String email;

    private String password;

    private Date memberSince;
}