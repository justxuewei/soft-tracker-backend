package com.niuxuewei.lucius.core.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum  ProjectUserRole {

    MEMBER("member"),
    MASTER("master"),
    TUTOR("tutor");

    @Getter
    private String role;


    @Override
    public String toString() {
        return this.role;
    }
}
