package com.niuxuewei.lucius.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class GetProjectsVO {

    // 项目ID
    private Integer id;

    // 项目名称
    private String name;

    // 项目状态
    private String status;

    // 项目成员
    private List<GetProjectMembersVO> members;

}
