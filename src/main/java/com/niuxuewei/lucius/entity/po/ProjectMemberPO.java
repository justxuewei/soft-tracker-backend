package com.niuxuewei.lucius.entity.po;

import lombok.Data;

@Data
public class ProjectMemberPO {
    private Integer id;

    private Integer userId;

    private Integer projectId;

    private String role;
}