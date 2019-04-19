package com.niuxuewei.lucius.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectMemberWithUserPO extends ProjectMemberPO {

    private UserPO userPO;

}
