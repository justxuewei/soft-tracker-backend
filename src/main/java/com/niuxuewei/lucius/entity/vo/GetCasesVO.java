package com.niuxuewei.lucius.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetCasesVO {

    private Integer id;

    private String title;

    private List<String> tags;

    private String briefIntro;

    private String avatar;

    private String author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date date;

}
