package com.niuxuewei.lucius.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CreateProjectDTO {

    // 案例案例
    @NotNull(message = "案例ID不能为空")
    private Integer caseId;

    // 项目成员ID
    @NotNull(message = "成员信息不能为空")
    private List<Integer> members;

    // 项目名称
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 50, message = "项目名称最长为50个字符")
    private String name;

    // 申请书的保存名称
    @NotBlank(message = "申请书信息不能为空")
    private String saveName;

    // 申请类型, college: 学院项目, enterprise: 校企合作项目
    @NotBlank(message = "申请类型不能为空")
    @Pattern(regexp = "^(college)|(enterprise)$", message = "申请类型不合法")
    private String type;

}
