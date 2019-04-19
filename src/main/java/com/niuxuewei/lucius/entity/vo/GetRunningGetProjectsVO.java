package com.niuxuewei.lucius.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetRunningGetProjectsVO extends GetProjectsVO {

    // 进度百分比，如50
    private String progress;

}
