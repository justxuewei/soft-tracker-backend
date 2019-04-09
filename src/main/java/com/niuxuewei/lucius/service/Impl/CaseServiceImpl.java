package com.niuxuewei.lucius.service.Impl;

import com.niuxuewei.lucius.core.exception.PermissionDeniedException;
import com.niuxuewei.lucius.core.utils.SecurityUtils;
import com.niuxuewei.lucius.entity.dto.CreateCaseDTO;
import com.niuxuewei.lucius.entity.po.CasePO;
import com.niuxuewei.lucius.entity.po.RolePO;
import com.niuxuewei.lucius.entity.po.UserPO;
import com.niuxuewei.lucius.entity.po.UserRolePO;
import com.niuxuewei.lucius.mapper.CasePOMapper;
import com.niuxuewei.lucius.mapper.RolePOMapper;
import com.niuxuewei.lucius.mapper.UserRolePOMapper;
import com.niuxuewei.lucius.service.ICaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    }
}
