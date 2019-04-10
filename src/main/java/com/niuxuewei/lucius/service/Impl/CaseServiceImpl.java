package com.niuxuewei.lucius.service.Impl;

import com.niuxuewei.lucius.core.exception.PermissionDeniedException;
import com.niuxuewei.lucius.core.utils.SecurityUtils;
import com.niuxuewei.lucius.core.utils.UserUtils;
import com.niuxuewei.lucius.entity.dto.CreateCaseDTO;
import com.niuxuewei.lucius.entity.po.*;
import com.niuxuewei.lucius.entity.vo.GetCaseDetailsVO;
import com.niuxuewei.lucius.entity.vo.GetCasesVO;
import com.niuxuewei.lucius.mapper.CasePOMapper;
import com.niuxuewei.lucius.mapper.CaseTagPOMapper;
import com.niuxuewei.lucius.mapper.RolePOMapper;
import com.niuxuewei.lucius.mapper.UserRolePOMapper;
import com.niuxuewei.lucius.service.ICaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CaseServiceImpl implements ICaseService {

    @Resource
    private UserRolePOMapper userRolePOMapper;

    @Resource
    private RolePOMapper rolePOMapper;

    @Resource
    private CasePOMapper casePOMapper;

    @Resource
    private CaseTagPOMapper caseTagPOMapper;

    @Override
    public void createCase(CreateCaseDTO createCaseDTO) {
        // 检查是都具有导师权限
        UserPO userPO = SecurityUtils.getUser();
        List<UserRolePO> userRolePOList = userRolePOMapper.selectByUserId(userPO.getId());
        List<RolePO> rolePOList = rolePOMapper.selectRoleByRoleIds(userRolePOList);

        boolean isTutor = false;
        for (RolePO rolePO: rolePOList) {
            if (rolePO.getRole().equals("tutor")) {
                isTutor = true;
                break;
            }
        }
        if (!isTutor) throw new PermissionDeniedException("无操作权限");

        // 插入数据
        CasePO casePO = new CasePO();
        casePO.setTitle(createCaseDTO.getTitle());
        casePO.setBriefIntro(createCaseDTO.getBriefIntro());
        casePO.setContent(createCaseDTO.getContent());
        if (createCaseDTO.getDemoUrl() != null) casePO.setDemoUrl(createCaseDTO.getDemoUrl());
        casePO.setAuthor(userPO.getId());
        casePO.setModifiedDate(new Date());

        System.out.println(casePO.getTitle());
        System.out.println(casePO.getBriefIntro());
        System.out.println(casePO.getContent());
        System.out.println(casePO.getDemoUrl());
        System.out.println(casePO.getAuthor());

        casePOMapper.insertSelective(casePO);

        if (createCaseDTO.getTags() == null) return;
        Integer caseId = casePO.getId();

        List<CaseTagPO> caseTagPOList = new ArrayList<>();
        for (String tag: createCaseDTO.getTags()) {
            CaseTagPO caseTagPO = new CaseTagPO();
            caseTagPO.setCaseId(caseId);
            caseTagPO.setTag(tag);
            caseTagPOList.add(caseTagPO);
        }

        caseTagPOMapper.insertList(caseTagPOList);
    }

    @Override
    public List<GetCasesVO> getCases(Integer tutorId) throws NoSuchAlgorithmException {
        List<CaseWithTagsPO> caseWithTagsPOList;
        if (tutorId == null) {
            caseWithTagsPOList = casePOMapper.selectWithTags();
        } else {
            caseWithTagsPOList = casePOMapper.selectWithTagsByTutorId(tutorId);
        }

        List<GetCasesVO> getCasesVOList = new ArrayList<>();

        for (CaseWithTagsPO c: caseWithTagsPOList) {
            GetCasesVO getCasesVO = new GetCasesVO();
            // id
            getCasesVO.setId(c.getId());
            // author username
            getCasesVO.setAuthor(c.getUser().getUsername());
            // avatar
            getCasesVO.setAvatar(UserUtils.avatar(c.getUser().getEmail()));
            // brief intro
            getCasesVO.setBriefIntro(c.getBriefIntro());
            // date
            getCasesVO.setDate(c.getModifiedDate());
            // title
            getCasesVO.setTitle(c.getTitle());
            // tags
            List<String> tags = new ArrayList<>();
            for (CaseTagPO caseTagPO: c.getCaseTagPOList()) {
                tags.add(caseTagPO.getTag());
            }
            getCasesVO.setTags(tags);
            getCasesVOList.add(getCasesVO);
        }

        return getCasesVOList;
    }

    @Override
    public GetCaseDetailsVO getCaseDetails(Integer caseId) throws NoSuchAlgorithmException {
        CaseWithTagsPO caseWithTagsPO = casePOMapper.selectFirstByCaseId(caseId);
        GetCaseDetailsVO getCaseDetailsVO = new GetCaseDetailsVO();
        // id
        getCaseDetailsVO.setId(caseWithTagsPO.getId());
        // title
        getCaseDetailsVO.setTitle(caseWithTagsPO.getTitle());
        // avatar
        System.out.println(caseWithTagsPO.getUser());
        System.out.println(caseWithTagsPO.getUser().getEmail());
        getCaseDetailsVO.setAvatar(UserUtils.avatar(caseWithTagsPO.getUser().getEmail()));
        // author
        getCaseDetailsVO.setAuthor(caseWithTagsPO.getUser().getUsername());
        // tags
        List<String> tags = new ArrayList<>();
        for (CaseTagPO caseTag: caseWithTagsPO.getCaseTagPOList()) {
            tags.add(caseTag.getTag());
        }
        getCaseDetailsVO.setTags(tags);
        // content
        getCaseDetailsVO.setContent(caseWithTagsPO.getContent());
        // demoUrl
        getCaseDetailsVO.setDemoUrl(caseWithTagsPO.getDemoUrl());
        // brief intro
        getCaseDetailsVO.setBriefIntro(caseWithTagsPO.getBriefIntro());
        return getCaseDetailsVO;
    }
}
