package com.niuxuewei.lucius.core.request;

public enum GitlabHttpRequestAuthMode {
    // headers不添加任何鉴权信息
    NO_AUTH,
    // 普通模式，在headers中添加Private-Token
    USER_AUTH,
    // 管理员模式，在headers中添加Authorization
    ADMIN_AUTH
}
