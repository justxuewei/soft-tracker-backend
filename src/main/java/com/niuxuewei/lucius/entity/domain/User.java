package com.niuxuewei.lucius.entity.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;

@Data
public class User {
    private Integer id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private String role;

    private Date memberSince;

    /**
     * 使用BCrypt加密密码
     */
    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }
}