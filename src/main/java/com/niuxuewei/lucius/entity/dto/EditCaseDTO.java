package com.niuxuewei.lucius.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class EditCaseDTO {

    @NotNull(message = "案例ID不能为空")
    private Integer id;

    @Size(max = 50, message = "标题最大长度为50字")
    private String title;

    @Size(max = 300, message = "简介最大长度为300字")
    private String briefIntro;

    private String content;

    private String demoUrl;

    private List<String> tags;

}
