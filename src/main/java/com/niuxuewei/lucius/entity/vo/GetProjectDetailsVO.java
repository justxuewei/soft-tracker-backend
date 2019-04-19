package com.niuxuewei.lucius.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetProjectDetailsVO {

    // 项目ID
    private Integer id;

    // 项目名称
    private String name;

    // 项目类型
    private String type;

    // 项目gitlab project id
    private Integer gitlabProjectId;

    // 案例ID
    private Integer caseId;

    // 当前状态
    private String status;

    private Object extraInfo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date updateTime;

    // 项目成员
    private List<GetProjectDetailsMembersVO> members;

}
