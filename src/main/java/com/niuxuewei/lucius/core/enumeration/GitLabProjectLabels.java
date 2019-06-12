package com.niuxuewei.lucius.core.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum GitLabProjectLabels {

    DISCUSS("discuss", "讨论"),
    MODULE("module", "模块");

    @Getter
    private String EN;

    @Getter
    private String CN;

}
