package com.niuxuewei.lucius.core.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ProjectType {

    COLLEGE("college"),
    ENTERPRISE("enterprise");

    @Getter
    private String type;


    @Override
    public String toString() {
        return this.type;
    }
}
