package com.niuxuewei.lucius.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImpersonationTokenDTO {

    private String token;

    private Long remainingTime;

}
