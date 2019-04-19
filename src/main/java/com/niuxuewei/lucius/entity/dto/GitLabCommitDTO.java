package com.niuxuewei.lucius.entity.dto;

import lombok.Data;

@Data
public class GitLabCommitDTO {

    private String id;

    private String committedDate;

    private String authorEmail;

}
