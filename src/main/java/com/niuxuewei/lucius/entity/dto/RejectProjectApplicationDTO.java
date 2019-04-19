package com.niuxuewei.lucius.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RejectProjectApplicationDTO {

    @NotBlank(message = "驳回理由不能为空")
    private String reason;

}
