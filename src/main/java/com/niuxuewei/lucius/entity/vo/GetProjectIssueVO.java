package com.niuxuewei.lucius.entity.vo;

import lombok.Data;

@Data
public class GetProjectIssueVO {

    private String title;

    private String project;

    private Integer projectId;

    private String webUrl;

    private String state;

}
