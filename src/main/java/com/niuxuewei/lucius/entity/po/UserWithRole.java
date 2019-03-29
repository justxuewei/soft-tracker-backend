package com.niuxuewei.lucius.entity.po;

import lombok.Data;

import java.util.List;

@Data
public class UserWithRole extends User {

    private List<Role> roleList;

}
