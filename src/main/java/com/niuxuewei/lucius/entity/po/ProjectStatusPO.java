package com.niuxuewei.lucius.entity.po;

import java.util.Date;
import lombok.Data;

@Data
public class ProjectStatusPO {
    private Integer id;

    private Integer projectId;

    private String status;

    private Object extraInfo;

    private Date updateTime;
}