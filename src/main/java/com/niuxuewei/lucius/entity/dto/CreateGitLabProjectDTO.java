package com.niuxuewei.lucius.entity.dto;

import lombok.Data;

@Data
public class CreateGitLabProjectDTO {

    // 项目名称
    private String name;

    // 项目ID
    private Integer projectId;

    // 组长对应的gitlab的用户Id
    private Integer gitlabUserId;

    /**
     * GitLab Project相关设置，除非特殊情况请保持默认
     */
    private boolean issuesEnabled = true;

    private boolean mergeRequestsEnabled = true;

    private boolean jobsEnabled = true;

    private boolean sharedRunnersEnabled = true;

    private String visibility = "private";

}
