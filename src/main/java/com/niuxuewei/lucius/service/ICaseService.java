package com.niuxuewei.lucius.service;

import com.niuxuewei.lucius.entity.dto.CreateCaseDTO;
import com.niuxuewei.lucius.entity.dto.EditCaseDTO;
import com.niuxuewei.lucius.entity.vo.GetCaseDetailsVO;
import com.niuxuewei.lucius.entity.vo.GetCasesVO;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ICaseService {

    void createCase(CreateCaseDTO createCaseDTO);

    List<GetCasesVO> getCases(Integer tutorId) throws NoSuchAlgorithmException;

    GetCaseDetailsVO getCaseDetails(Integer caseId) throws NoSuchAlgorithmException;

    void checkAuth(Integer caseId);

    void editCase(EditCaseDTO editCaseDTO);

}
