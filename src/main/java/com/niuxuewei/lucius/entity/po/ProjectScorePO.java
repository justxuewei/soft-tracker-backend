package com.niuxuewei.lucius.entity.po;

import java.util.Date;
import lombok.Data;

@Data
public class ProjectScorePO {
    private Integer projectId;

    private Double contribution;

    private Double codeQuality;

    private Double defence;

    private Date submitDate;
}