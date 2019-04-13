package com.niuxuewei.lucius.service.Impl;

import com.alibaba.fastjson.JSON;
import com.niuxuewei.lucius.core.enumeration.ProjectStatus;
import com.niuxuewei.lucius.core.enumeration.ProjectUserRole;
import com.niuxuewei.lucius.core.enumeration.UserRole;
import com.niuxuewei.lucius.core.exception.*;
import com.niuxuewei.lucius.core.utils.SecurityUtils;
import com.niuxuewei.lucius.core.utils.StringUtils;
import com.niuxuewei.lucius.entity.dto.CreateProjectDTO;
import com.niuxuewei.lucius.entity.po.*;
import com.niuxuewei.lucius.entity.vo.ApplicationUploadVO;
import com.niuxuewei.lucius.entity.vo.CreateProjectVO;
import com.niuxuewei.lucius.mapper.*;
import com.niuxuewei.lucius.service.IProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@PropertySource("classpath:lucius-config.properties")
@Transactional(rollbackFor = Exception.class)
public class ProjectServiceImpl implements IProjectService {

    @Value("${soft-tracker.project-application.upload-folder}")
    private String UPLOAD_FOLDER;

    @Value("${soft-tracker.project-application.allowed-suffix}")
    private String ALLOWED_SUFFIX;

    @Resource
    private CasePOMapper casePOMapper;

    @Resource
    private ProjectPOMapper projectPOMapper;

    @Resource
    private UserPOMapper userPOMapper;

    @Resource
    private ProjectMemberPOMapper projectMemberPOMapper;

    @Resource
    private ProjectStatusPOMapper projectStatusPOMapper;

    private List<String> getAllowedSuffix() {
        return Arrays.asList(ALLOWED_SUFFIX.split(","));
    }

    @Override
    public ApplicationUploadVO applicationUpload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidParamException("文件不能为空");
        }

        if (file.getOriginalFilename() == null) {
            throw new InvalidParamException("文件类型不合法");
        } else {
            boolean flag = false;
            for (String suffix: getAllowedSuffix()) {
                if (suffix.equals(StringUtils.getFileSuffix(file.getOriginalFilename()))) {
                    flag = true;
                    break;
                }
            }
            if (!flag) throw new InvalidParamException("文件类型不合法，" +
                    "请上传后缀名为" + String.join(", ", getAllowedSuffix() + "的文件"));
        }

        try {
            byte[] bytes = file.getBytes();
            String fileMd5 = StringUtils.md5(bytes);
            String fileSuffix = StringUtils.getFileSuffix(file.getOriginalFilename());
            String pathString = UPLOAD_FOLDER + fileMd5 + "." + fileSuffix;

            ApplicationUploadVO applicationUploadVO = new ApplicationUploadVO();
            applicationUploadVO.setOriginName(file.getOriginalFilename());
            applicationUploadVO.setOriginNameWithoutSuffix(StringUtils.getFileNameWithoutSuffix(file.getOriginalFilename()));
            applicationUploadVO.setSaveName(fileMd5 + "." + fileSuffix);
            applicationUploadVO.setSaveNameWithoutSuffix(fileMd5);
            applicationUploadVO.setSuffix(fileSuffix);

            File serverFile = new File(pathString);
            // 已经上传，没必要再传一遍了
            if (serverFile.exists()) {
                log.debug("文件已经上传过了，无需再次上传");
                return applicationUploadVO;
            }
            Path path = Paths.get(pathString);
            Files.write(path, bytes);
            return applicationUploadVO;
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalErrorException();
        }
    }

    @Override
    public CreateProjectVO createProject(CreateProjectDTO createProjectDTO) {
        // 检查case是否存在
        Integer caseId = createProjectDTO.getCaseId();
        CaseWithTagsPO casePO = casePOMapper.selectFirstByCaseId(caseId);
        if (casePO == null) {
            throw new NotFoundException("使用的案例不合法");
        }

        UserPO tutor = casePO.getUser();

        // 检测申报书是否存在
        File applicationFile = new File(UPLOAD_FOLDER + createProjectDTO.getSaveName());
        if (!applicationFile.exists()) throw new NotFoundException("申报书不合法");

        Date currentDate = new Date();

        ProjectPO projectPO = new ProjectPO();
        projectPO.setCaseId(caseId);
        projectPO.setName(createProjectDTO.getName());
        projectPO.setType(createProjectDTO.getType());
        projectPO.setUpdateTime(currentDate);
        projectPO.setCreateTime(currentDate);
        projectPOMapper.insertSelective(projectPO);

        Integer projectId = projectPO.getId();

        // 插入成员
        List <ProjectMemberPO> projectMemberPOList = new ArrayList<>();
        for (Integer uid: createProjectDTO.getMembers()) {
            // 检查成员是否存在
            UserWithRolePO member = userPOMapper.selectFirstById(uid);
            if (member == null) {
                log.error("成员信息非法，成员(id={})不存在", uid);
                throw new NotFoundException("成员信息非法");
            }

            if (member.getId().equals(SecurityUtils.getUserId())) {
                log.error("成员信息非法，不能自己添加自己");
                throw new ForbiddenException("成员信息非法");
            }

            // 检查成员角色是否为学生
            for (RolePO rolePO: member.getRoles()) {
                if (!rolePO.getRole().equals(UserRole.STUDENT.getRole())) {
                    log.error("成员角色非法，成员ID为: {}，需要的角色为: {}，实际角色为: {}"
                            , uid, UserRole.STUDENT.getRole(), rolePO.getRole());
                    throw new ForbiddenException("成员角色非法");
                }
            }
            // 检查无误后提交信息
            ProjectMemberPO projectMemberPO = new ProjectMemberPO();
            projectMemberPO.setProjectId(projectId);
            projectMemberPO.setRole(ProjectUserRole.MEMBER.getRole());
            projectMemberPO.setUserId(uid);
            projectMemberPOList.add(projectMemberPO);
        }

        // 插入组长信息
        ProjectMemberPO projectMaster = new ProjectMemberPO();
        projectMaster.setProjectId(projectId);
        projectMaster.setRole(ProjectUserRole.MASTER.getRole());
        projectMaster.setUserId(SecurityUtils.getUserId());
        projectMemberPOList.add(projectMaster);

        // 插入导师信息
        ProjectMemberPO projectTutor = new ProjectMemberPO();
        projectTutor.setUserId(tutor.getId());
        projectTutor.setProjectId(projectId);
        projectTutor.setRole(ProjectUserRole.TUTOR.getRole());
        projectMemberPOList.add(projectTutor);

        projectMemberPOMapper.insertListSelective(projectMemberPOList);

        // 更新状态
        ProjectStatusPO projectStatusPO = new ProjectStatusPO();
        projectStatusPO.setProjectId(projectId);
        projectStatusPO.setStatus(ProjectStatus.APPLYING.getStatus());
        projectStatusPO.setUpdateTime(currentDate);
        ApplyingExtraInfoPO applyingExtraInfoPO = new ApplyingExtraInfoPO();
        applyingExtraInfoPO.setSaveName(createProjectDTO.getSaveName());
        projectStatusPO.setExtraInfo(JSON.toJSONString(applyingExtraInfoPO));
        projectStatusPOMapper.insertSelective(projectStatusPO);

        return new CreateProjectVO(projectId);
    }

    @Override
    public void deleteProject(Integer projectId) {
        // 检查权限，仅有导师和组长可以删除该项目
        Integer uid = SecurityUtils.getUserId();
        ProjectMemberPO memberInfo = projectMemberPOMapper.selectFirstByProjectIdAndUserId(projectId, uid);
        if (memberInfo == null) {
            throw new PermissionDeniedException("无权限");
        }
        // 既不是导师也不是组长的无权限删除该项目
        if (!memberInfo.getRole().equals(ProjectUserRole.MASTER.getRole()) &&
                !memberInfo.getRole().equals(ProjectUserRole.TUTOR.getRole())) {
            throw new PermissionDeniedException("无权限");
        }

        // TODO: 记得删除gitlab中的相关项目
        projectPOMapper.deleteByPrimaryKey(projectId);
        projectStatusPOMapper.deleteByProjectId(projectId);
        projectMemberPOMapper.deleteByProjectId(projectId);
    }

}
