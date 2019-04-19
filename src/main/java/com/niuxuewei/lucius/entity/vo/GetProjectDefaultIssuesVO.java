package com.niuxuewei.lucius.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class GetProjectDefaultIssuesVO {

    List<GetProjectIssueVO> modules;

    List<GetProjectIssueVO> discussions;

}
