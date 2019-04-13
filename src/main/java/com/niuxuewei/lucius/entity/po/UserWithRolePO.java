package com.niuxuewei.lucius.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserWithRolePO extends UserPO {

    private List<RolePO> roles;

}
