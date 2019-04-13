package com.niuxuewei.lucius.entity.vo;

import lombok.Data;

@Data
public class ApplicationUploadVO {

    private String originName;

    private String saveName;

    private String suffix;

    private String originNameWithoutSuffix;

    private String saveNameWithoutSuffix;

}
