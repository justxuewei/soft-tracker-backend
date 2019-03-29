package com.niuxuewei.lucius.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AuthLoginDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 6, max = 50)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50)
    private String password;

}
