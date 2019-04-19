package com.niuxuewei.lucius.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ReapplyProjectDTO {

    @NotNull(message = "项目ID不能为空")
    private Integer id;

    @Size(max = 50, message = "项目名称最长为50个字符")
    private String name;

    @Pattern(regexp = "^(college)|(enterprise)$", message = "申请类型不合法")
    private String type;

    private List<Integer> members;

    private String saveName;


}
