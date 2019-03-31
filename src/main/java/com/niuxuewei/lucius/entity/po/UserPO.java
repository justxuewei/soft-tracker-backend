package com.niuxuewei.lucius.entity.po;

import java.util.Date;
import lombok.Data;

@Data
public class UserPO {
    private Integer id;

    private String username;

    private String email;

    private String password;

    private Date memberSince;
}