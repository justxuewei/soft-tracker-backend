package com.niuxuewei.lucius.core.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GitLabProjectAccessLevels {

    GUEST(10),
    REPORTER(20),
    DEVELOPER(30),
    MAINTAINER(40);

    private Integer code;

}
