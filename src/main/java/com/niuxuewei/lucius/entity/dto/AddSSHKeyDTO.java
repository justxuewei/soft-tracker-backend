package com.niuxuewei.lucius.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddSSHKeyDTO {

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "SSH Key不能为空")
    private String key;

}
