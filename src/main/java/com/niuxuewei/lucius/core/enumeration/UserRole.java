package com.niuxuewei.lucius.core.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UserRole {

    STUDENT("student"),
    TUTOR("tutor");

    @Getter
    private String role;

}
