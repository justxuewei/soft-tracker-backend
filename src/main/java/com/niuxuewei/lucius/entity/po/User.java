package com.niuxuewei.lucius.entity.po;

import java.util.Date;
import lombok.Data;

@Data
public class User {
    private Integer id;

    private String username;

    private String email;

    private String password;

    private String role;

    private Date memberSince;
}