package com.niuxuewei.lucius.entity.po;

import java.util.Date;
import lombok.Data;

@Data
public class ProjectPO {
    private Integer id;

    private String name;

    private String type;

    private Integer caseId;

    private Integer gitlabProjectId;

    private Date updateTime;

    private Date createTime;
}