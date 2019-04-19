package com.niuxuewei.lucius.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectWithMembersDetailsAndStatusPO extends ProjectPO {

    private List<ProjectMemberWithUserPO> membersPO;

    private ProjectStatusPO statusPO;

}
