package com.niuxuewei.lucius.core.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum GitLabCIStatus {

    NO_INFO("no-info"),
    SUCCESS("success"),
    FAIL("fail");

    @Getter
    private String status;

}
