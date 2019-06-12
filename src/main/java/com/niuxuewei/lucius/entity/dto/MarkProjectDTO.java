package com.niuxuewei.lucius.entity.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class MarkProjectDTO {

    @NotNull(message = "代码质量得分不能为空")
    @Min(value = 0, message = "代码质量得分区间为0-100")
    @Max(value = 100, message = "代码质量得分区间为0-100")
    private Double codeQualityScore;

    @NotNull(message = "答辩得分不能为空")
    @Min(value = 0, message = "答辩得分区间为0-100")
    @Max(value = 100, message = "答辩得分区间为0-100")
    private Double defenceScore;

}
