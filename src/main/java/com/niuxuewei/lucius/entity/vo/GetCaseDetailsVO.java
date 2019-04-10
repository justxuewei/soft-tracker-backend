package com.niuxuewei.lucius.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class GetCaseDetailsVO {

    private Integer id;

    private String title;

    private String briefIntro;

    private String avatar;

    private String author;

    private List<String> tags;

    private String content;

    private String demoUrl;

}
