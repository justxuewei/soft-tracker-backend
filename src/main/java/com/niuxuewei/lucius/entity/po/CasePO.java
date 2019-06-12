package com.niuxuewei.lucius.entity.po;

import java.util.Date;
import lombok.Data;

@Data
public class CasePO {
    private Integer id;

    private String title;

    private String briefIntro;

    private String content;

    private String demoUrl;

    private Integer author;

    private Date modifiedDate;
}