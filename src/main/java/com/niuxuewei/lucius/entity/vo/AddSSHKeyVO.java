package com.niuxuewei.lucius.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AddSSHKeyVO {

    private Integer id;

    private String title;

    private String key;

    @JSONField(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Beijing")
    private Date createdAt;

}
