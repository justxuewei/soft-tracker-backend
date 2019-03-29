package com.niuxuewei.lucius.entity.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class AuthRegisterDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 6, max = 50, message = "用户名最小长度为6，最大长度为50")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码最小长度为6，最大长度为50")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;

    private List<String> roles;

}
