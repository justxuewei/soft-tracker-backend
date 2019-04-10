package com.niuxuewei.lucius.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CaseWithTagsPO extends CasePO {

    private List<CaseTagPO> caseTagPOList;

    private UserPO user;

}
