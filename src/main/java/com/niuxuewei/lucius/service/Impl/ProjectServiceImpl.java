package com.niuxuewei.lucius.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.niuxuewei.lucius.core.enumeration.*;
import com.niuxuewei.lucius.core.exception.*;
import com.niuxuewei.lucius.core.request.GitlabHttpRequest;
import com.niuxuewei.lucius.core.utils.DateUtils;
import com.niuxuewei.lucius.core.utils.SecurityUtils;
import com.niuxuewei.lucius.core.utils.StringUtils;
import com.niuxuewei.lucius.entity.dto.*;
import com.niuxuewei.lucius.entity.po.*;
import com.niuxuewei.lucius.entity.vo.*;
import com.niuxuewei.lucius.mapper.*;
import com.niuxuewei.lucius.service.IProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@PropertySource("classpath:lucius-config.properties")
@Transactional(rollbackFor = Exception.class)
public class ProjectServiceImpl implements IProjectService {

    @Value("${soft-tracker.project-application.upload-folder}")
    private String UPLOAD_FOLDER;

    @Value("${soft-tracker.project-application.allowed-suffix}")
    private String ALLOWED_SUFFIX;

    @Value("${sonar.report}")
    private String SONAR_REPORT;

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

    @Resource
    private GitlabHttpRequest gitlabHttpRequest;

    @Resource
    private GitlabUserPOMapper gitlabUserPOMapper;

    @Resource
    private ProjectScorePOMapper projectScorePOMapper;

    private List<String> getAllowedSuffix() {
        return Arrays.asList(ALLOWED_SUFFIX.split(","));
    }

    /**
     * 将ProjectMember列表装换为GetProjectsProjectMemberVO列表
     */
    private List<GetProjectMembersVO> convertToGetProjectsProjectMemberVO(
            List<ProjectMemberPO> projectMemberPOList) {
        List<GetProjectMembersVO> members = new ArrayList<>();
        for (ProjectMemberPO memberPO : projectMemberPOList) {
            GetProjectMembersVO getProjectMembersVO = new GetProjectMembersVO();
            getProjectMembersVO.setUserId(memberPO.getUserId());
            getProjectMembersVO.setRole(memberPO.getRole());
            members.add(getProjectMembersVO);
        }
        return members;
    }

    private JSONArray getProjectsIssuesByLabel(Integer gitlabProjectId, String label) {
        String listString = gitlabHttpRequest.get(GitLabHttpRequestAuthMode.USER_AUTH,
                String.format("/projects/%d/issues?labels=%s",
                        gitlabProjectId, label));
        return JSON.parseArray(listString);
    }

    private String getProjectProgress(Integer gitlabProjectId) {
        JSONArray issues = getProjectsIssuesByLabel(gitlabProjectId, GitLabProjectLabels.MODULE.getEN());
        int closed = 0;
        int total = 0;
        for (Object item : issues) {
            JSONObject issue = (JSONObject) item;
            String state = issue.getString("state");
            if ("closed".equals(state)) closed++;
            total++;
        }
        if (total == 0) {
            log.debug("项目(gitlabProjectId: {})无issues", gitlabProjectId);
            return "0";
        } else {
            log.debug("项目(gitlabProjectId: {})状态为opened的issues有: {}，总共有: {}", gitlabProjectId, closed, total);
            double percentage = ((double) closed / (double) total) * 100;
            // 四舍五入后删除小数点
            return new BigDecimal(percentage).setScale(0, RoundingMode.UP).toString();
        }
    }

    /**
     * 获取正在进行的项目
     *
     * @param pids 用户全部项目列表
     */
    private List<GetRunningGetProjectsVO> getRunningProjectsVOList(List<Integer> pids) {
        List<GetRunningGetProjectsVO> projects = new ArrayList<>();
        for (Integer id : pids) {
            GetRunningGetProjectsVO runningProject = new GetRunningGetProjectsVO();
            ProjectWithMembersAndStatusPO project = projectPOMapper.selectFirstProjectWithoutUserDetailsByProjectId(id);
            if (project.getStatusPO().getStatus().equals(ProjectStatus.CLOSED.getStatus())) continue;
            runningProject.setId(id);
            runningProject.setName(project.getName());
            runningProject.setStatus(project.getStatusPO().getStatus());
            runningProject.setMembers(convertToGetProjectsProjectMemberVO(project.getMembers()));
            // 获取任务进度
            if (project.getGitlabProjectId() != null)
                runningProject.setProgress(getProjectProgress(project.getGitlabProjectId()));
            else runningProject.setProgress("0");
            projects.add(runningProject);
        }
        return projects;
    }

    private List<ProjectMemberPO> insertMembers(List<Integer> userIds, Integer projectId, List<ProjectMemberPO> members) {
        for (Integer uid : userIds) {
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
            for (RolePO rolePO : member.getRoles()) {
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
            members.add(projectMemberPO);
        }
        return members;
    }

    private List<GetClosedGetProjectsVO> getClosedProjectsVOList(List<Integer> pids) {
        List<GetClosedGetProjectsVO> projects = new ArrayList<>();
        for (Integer id : pids) {
            GetClosedGetProjectsVO closedProject = new GetClosedGetProjectsVO();
            ProjectWithMembersAndStatusPO project = projectPOMapper.selectFirstProjectWithoutUserDetailsByProjectId(id);
            // 非已关闭的项目则关闭
            if (!ProjectStatus.CLOSED.getStatus().equals(project.getStatusPO().getStatus())) continue;
            closedProject.setId(id);
            closedProject.setName(project.getName());
            closedProject.setStatus(project.getStatusPO().getStatus());
            closedProject.setMembers(convertToGetProjectsProjectMemberVO(project.getMembers()));
            // 添加分数
            ProjectScorePO projectScorePO = projectScorePOMapper.selectByPrimaryKey(id);
            closedProject.setScore(getProjectTotalScore(projectScorePO.getContribution(), projectScorePO.getCodeQuality(), projectScorePO.getDefence()));
            projects.add(closedProject);
        }
        return projects;
    }

    private void memberAuth(List<? extends ProjectMemberPO> members) {
        for (ProjectMemberPO member : members) {
            if (SecurityUtils.getUserId().equals(member.getUserId())) {
                return;
            }
        }
        throw new PermissionDeniedException("你无权限进行该操作");
    }

    private void tutorAuth(List<ProjectMemberWithUserPO> members) {
        for (ProjectMemberWithUserPO member : members) {
            if (SecurityUtils.getUserId().equals(member.getUserId())
                    && member.getRole().equals(ProjectUserRole.TUTOR.getRole())) {
                return;
            }
        }
        throw new PermissionDeniedException("你无权限进行该操作");
    }

    private void tutorAuth(ProjectMemberPO member) {
        if (SecurityUtils.getUserId().equals(member.getUserId())
                && member.getRole().equals(ProjectUserRole.TUTOR.getRole())) {
            return;
        }
        throw new PermissionDeniedException("你无权限进行该操作");
    }

    private void masterAuth(ProjectMemberPO member) {
        if (member == null) throw new PermissionDeniedException("你无权限进行该操作");
        if (SecurityUtils.getUserId().equals(member.getUserId())
                && member.getRole().equals(ProjectUserRole.MASTER.getRole())) return;
        throw new PermissionDeniedException("你无权限进行该操作");
    }

    private void masterAuth(List<? extends ProjectMemberPO> members) {
        for (ProjectMemberPO member : members) {
            if (SecurityUtils.getUserId().equals(member.getUserId())) {
                if (member.getRole().equals(ProjectUserRole.MASTER.getRole())) return;
            }
        }
        throw new PermissionDeniedException("你无权限进行该操作");
    }

    /**
     * 检测当前状态
     *
     * @param projectId 项目ID
     * @param status    期望的当前状态
     */
    private void checkStatus(Integer projectId, ProjectStatus status) {
        ProjectStatusPO lastStatus = projectStatusPOMapper.selectFirstByProjectIdOrderByUpdateTimeDesc(projectId);
        if (!lastStatus.getStatus().equals(status.getStatus())) {
            log.error("非法操作，当前状态: {}，期望状态: {}，用户ID: {}", lastStatus.getStatus(), status.getStatus(), SecurityUtils.getUserId());
            throw new ForbiddenException("非法操作");
        }
    }

    private void checkStatus(ProjectWithMembersDetailsAndStatusPO project, ProjectStatus status) {
        checkStatus(project.getStatusPO(), status);
    }

    private void checkStatus(ProjectStatusPO projectStatus, ProjectStatus status) {
        if (!projectStatus.getStatus().equals(status.getStatus())) {
            log.error("非法操作，当前状态: {}，期望状态: {}，用户ID: {}",
                    projectStatus.getStatus(), status.getStatus(), SecurityUtils.getUserId());
            throw new ForbiddenException("非法操作");
        }
    }

    /**
     * 创建GitLab项目
     * GitLab项目名称为SoftTracker项目名称
     * GitLab Path为项目ID
     *
     * @return gitlabId
     */
    private Integer createGitLabProject(CreateGitLabProjectDTO dto) {
        String resString = gitlabHttpRequest.post(GitLabHttpRequestAuthMode.ADMIN_AUTH,
                "/projects/user/" + dto.getGitlabUserId(),
                new LinkedMultiValueMap<String, String>() {
                    private static final long serialVersionUID = 811186616388849551L;

                    {
                        add("name", dto.getName());
                        add("path", dto.getProjectId().toString());
                        add("issues_enabled", StringUtils.booleanToString(dto.isIssuesEnabled()));
                        add("merge_requests_enabled", StringUtils.booleanToString(dto.isMergeRequestsEnabled()));
                        add("jobs_enabled", StringUtils.booleanToString(dto.isJobsEnabled()));
                        add("shared_runners_enabled", StringUtils.booleanToString(dto.isSharedRunnersEnabled()));
                        add("visibility", dto.getVisibility());
                    }
                });
        log.debug("创建GitLab项目后返回的数据为: {}", resString);
        JSONObject res = JSON.parseObject(resString);
        return res.getInteger("id");
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
            for (String suffix : getAllowedSuffix()) {
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
            log.debug("Upload Path: {}", pathString);

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

        // 检查题目是否重复
        ProjectPO deduplicatedProject = projectPOMapper.selectFirstByName(createProjectDTO.getName());
        if (deduplicatedProject != null) throw new ForbiddenException("题目重复，请换个题目后重试");

        Date currentDate = new Date();

        ProjectPO projectPO = new ProjectPO();
        projectPO.setCaseId(caseId);
        projectPO.setName(createProjectDTO.getName());
        projectPO.setType(createProjectDTO.getType());
        projectPO.setCreateTime(currentDate);
        projectPOMapper.insertSelective(projectPO);

        Integer projectId = projectPO.getId();

        // 插入成员
        List<ProjectMemberPO> projectMemberPOList = insertMembers(
                createProjectDTO.getMembers(), projectId, new ArrayList<>());

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

        // TODO: 删除gitlab中的相关项目
        projectPOMapper.deleteByPrimaryKey(projectId);
        projectStatusPOMapper.deleteByProjectId(projectId);
        projectMemberPOMapper.deleteByProjectId(projectId);
    }

    @Override
    public List<? extends GetProjectsVO> getProjects(String type) {
        Integer uid = SecurityUtils.getUserId();
        List<Integer> userProjectIds = projectMemberPOMapper.selectProjectIdByUserId(uid);

        List<? extends GetProjectsVO> retData;
        if ("running".equals(type)) {
            retData = getRunningProjectsVOList(userProjectIds);
        } else if ("closed".equals(type)) {
            retData = getClosedProjectsVOList(userProjectIds);
        } else {
            // 目前仅接收type为running或closed的项目列表
            throw new InvalidParamException();
        }

        return retData;
    }

    @Override
    public GetProjectDetailsVO getProjectDetails(Integer id) {
        ProjectWithMembersDetailsAndStatusPO projectDetails = projectPOMapper.selectFirstProjectByProjectId(id);
        if (projectDetails == null) {
            throw new NotFoundException();
        }

        memberAuth(projectDetails.getMembersPO());

        GetProjectDetailsVO retProjectDetails = new GetProjectDetailsVO();
        retProjectDetails.setId(id);
        retProjectDetails.setCaseId(projectDetails.getCaseId());
        retProjectDetails.setGitlabProjectId(projectDetails.getGitlabProjectId());

        List<GetProjectDetailsMembersVO> members = new ArrayList<>();
        for (ProjectMemberWithUserPO member : projectDetails.getMembersPO()) {
            GetProjectDetailsMembersVO m = new GetProjectDetailsMembersVO();
            m.setId(member.getUserPO().getId());
            m.setEmail(member.getUserPO().getEmail());
            m.setUsername(member.getUserPO().getUsername());
            m.setProjectRole(member.getRole());
            m.setRealname(member.getUserPO().getRealname());
            members.add(m);
        }

        retProjectDetails.setMembers(members);
        retProjectDetails.setName(projectDetails.getName());
        retProjectDetails.setType(projectDetails.getType());
        retProjectDetails.setStatus(projectDetails.getStatusPO().getStatus());
        retProjectDetails.setUpdateTime(projectDetails.getStatusPO().getUpdateTime());
        retProjectDetails.setExtraInfo(JSON.parseObject((String) projectDetails.getStatusPO().getExtraInfo()));

        return retProjectDetails;

    }

    @Override
    public void rejectProjectApplication(Integer id, RejectProjectApplicationDTO rejectProjectApplicationDTO) {
        ProjectMemberPO memberPO = projectMemberPOMapper
                .selectFirstByProjectIdAndUserId(id, SecurityUtils.getUserId());
        tutorAuth(memberPO);

        // 检查目前状态是否为applying
        checkStatus(id, ProjectStatus.APPLYING);

        ProjectStatusPO projectStatusPO = new ProjectStatusPO();
        projectStatusPO.setProjectId(id);
        projectStatusPO.setUpdateTime(new Date());
        projectStatusPO.setExtraInfo(JSON.toJSONString(rejectProjectApplicationDTO));
        projectStatusPO.setStatus(ProjectStatus.REJECTED.getStatus());

        projectStatusPOMapper.insertSelective(projectStatusPO);
    }

    @Override
    public void reapplyProject(ReapplyProjectDTO reapplyProjectDTO) {
        // 检测是否为组长权限
        ProjectMemberPO master = projectMemberPOMapper
                .selectFirstByProjectIdAndUserId(reapplyProjectDTO.getId(), SecurityUtils.getUserId());
        masterAuth(master);

        // 检测状态是否为rejected
        checkStatus(reapplyProjectDTO.getId(), ProjectStatus.REJECTED);

        ProjectPO projectPO = null;

        if (reapplyProjectDTO.getName() != null) {
            projectPO = new ProjectPO();
            projectPO.setName(reapplyProjectDTO.getName());
        }

        if (reapplyProjectDTO.getType() != null) {
            if (projectPO == null) projectPO = new ProjectPO();
            projectPO.setType(reapplyProjectDTO.getType());
        }

        if (projectPO != null) {
            projectPO.setId(reapplyProjectDTO.getId());
            // 更新project表信息
            projectPOMapper.updateByPrimaryKeySelective(projectPO);
        }

        if (reapplyProjectDTO.getMembers() != null) {
            // 删除全部member级别成员
            projectMemberPOMapper.deleteByProjectIdAndRole(reapplyProjectDTO.getId(), ProjectUserRole.MEMBER.getRole());
            // 逐一添加成员
            List<ProjectMemberPO> members = insertMembers(
                    reapplyProjectDTO.getMembers(), reapplyProjectDTO.getId(), new ArrayList<>());
            projectMemberPOMapper.insertListSelective(members);
        }

        String saveName = reapplyProjectDTO.getSaveName();
        if (saveName == null) {
            // 从上一个申请中获取saveName
            ProjectStatusPO lastStatus = projectStatusPOMapper.selectFirstByProjectIdAndStatusOrderByUpdateTimeDesc(
                    reapplyProjectDTO.getId(), ProjectStatus.APPLYING.getStatus());
            saveName = JSON.parseObject((String) lastStatus.getExtraInfo()).getString("saveName");
        }
        ApplyingExtraInfoPO applyingExtraInfoPO = new ApplyingExtraInfoPO();
        applyingExtraInfoPO.setSaveName(saveName);

        // 更新状态
        ProjectStatusPO status = new ProjectStatusPO();
        status.setProjectId(reapplyProjectDTO.getId());
        status.setStatus(ProjectStatus.APPLYING.getStatus());
        status.setExtraInfo(JSON.toJSONString(applyingExtraInfoPO));
        status.setUpdateTime(new Date());
        projectStatusPOMapper.insertSelective(status);

    }

    private Integer getMasterGitLabId(List<ProjectMemberWithUserPO> members) {
        ProjectMemberWithUserPO master = null;
        for (ProjectMemberWithUserPO member : members) {
            if (member.getRole().equals(ProjectUserRole.MASTER.getRole())) {
                master = member;
                break;
            }
        }
        if (master == null) throw new NotFoundException("未找到组长");

        Integer masterId = master.getUserId();
        GitlabUserPO masterGitlabUserPO = gitlabUserPOMapper.selectFirstByUserId(masterId);

        if (masterGitlabUserPO == null) throw new NotFoundException("未找到组长GitLab账户");

        return masterGitlabUserPO.getGitlabId();
    }

    private void addMembersIntoGitLabProject(Integer gitlabProjectId,
                                             List<ProjectMemberWithUserPO> members) {
        for (ProjectMemberWithUserPO member : members) {
            // 略过组长
            if (member.getRole().equals(ProjectUserRole.MASTER.getRole())) continue;
            GitlabUserPO gitlabUserPO = gitlabUserPOMapper.selectFirstByUserId(member.getUserId());
            Integer gitlabUserId = gitlabUserPO.getGitlabId();
            String url = String.format("/projects/%d/members", gitlabProjectId);
            gitlabHttpRequest.post(GitLabHttpRequestAuthMode.ADMIN_AUTH, url, new LinkedMultiValueMap<String, String>() {
                private static final long serialVersionUID = 6382931776384592078L;

                {
                    add("user_id", gitlabUserId.toString());
                    add("access_level", GitLabProjectAccessLevels.DEVELOPER.getCode().toString());
                }
            });
        }
    }

    private void addDefaultLabels(Integer gitlabProjectId) {
        String url = String.format("/projects/%d/labels", gitlabProjectId);
        gitlabHttpRequest.post(GitLabHttpRequestAuthMode.ADMIN_AUTH, url, new LinkedMultiValueMap<String, String>() {
            private static final long serialVersionUID = 3922714134167464975L;

            {
                add("name", GitLabProjectLabels.DISCUSS.getEN());
                add("color", "#69D100");
            }
        });
        gitlabHttpRequest.post(GitLabHttpRequestAuthMode.ADMIN_AUTH, url, new LinkedMultiValueMap<String, String>() {
            private static final long serialVersionUID = 4043984251168481943L;

            {
                add("name", GitLabProjectLabels.MODULE.getEN());
                add("color", "#69D100");
            }
        });
    }

    @Override
    public void acceptProjectApplication(Integer id) {
        // 检测是否是该项目的导师
        ProjectWithMembersDetailsAndStatusPO project = projectPOMapper.selectFirstProjectByProjectId(id);
        tutorAuth(project.getMembersPO());

        // 检测当前状态是否为applying
        checkStatus(id, ProjectStatus.APPLYING);

        // 创建Gitlab项目
        CreateGitLabProjectDTO createGitLabProjectDTO = new CreateGitLabProjectDTO();
        createGitLabProjectDTO.setName(project.getName());
        createGitLabProjectDTO.setProjectId(project.getId());
        createGitLabProjectDTO.setGitlabUserId(getMasterGitLabId(project.getMembersPO()));
        Integer gitlabProjectId = createGitLabProject(createGitLabProjectDTO);

        // 将gitlab project id保存到project表中
        ProjectPO projectPO = new ProjectPO();
        projectPO.setId(project.getId());
        projectPO.setGitlabProjectId(gitlabProjectId);
        projectPOMapper.updateByPrimaryKeySelective(projectPO);

        // 添加成员
        addMembersIntoGitLabProject(gitlabProjectId, project.getMembersPO());

        // 添加默认Label
        addDefaultLabels(gitlabProjectId);

        // 更新状态
        ProjectStatusPO statusPO = new ProjectStatusPO();
        statusPO.setProjectId(project.getId());
        statusPO.setStatus(ProjectStatus.DEVELOPING.getStatus());
        statusPO.setUpdateTime(new Date());
        projectStatusPOMapper.insertSelective(statusPO);
    }

    @Override
    public List<GetProjectIssueVO> getProjectDiscuss() {
        List<ProjectPO> projectPOList = projectPOMapper.selectByUserId(SecurityUtils.getUserId());
        List<GetProjectIssueVO> projectDiscussList = new ArrayList<>();
        for (ProjectPO project : projectPOList) {
            JSONArray issues = getProjectsIssuesByLabel(
                    project.getGitlabProjectId(), GitLabProjectLabels.DISCUSS.getEN());
            for (Object item : issues) {
                JSONObject issue = (JSONObject) item;
                if ("opened".equals(issue.getString("state"))) {
                    GetProjectIssueVO discuss = new GetProjectIssueVO();
                    discuss.setTitle(issue.getString("title"));
                    discuss.setProject(project.getName());
                    discuss.setProjectId(project.getId());
                    discuss.setWebUrl(issue.getString("web_url"));
                    projectDiscussList.add(discuss);
                }
            }
        }
        return projectDiscussList;
    }

    @Override
    public GetProjectDefaultIssuesVO getProjectDefaultIssues(Integer id) {
        // 检查是否有权限
        ProjectPO project = projectPOMapper.selectFirstByProjectIdAndUserId(id, SecurityUtils.getUserId());
        if (project == null) throw new PermissionDeniedException("你无权查看该信息");

        JSONArray data = getProjectsIssuesByLabel(project.getGitlabProjectId(), GitLabProjectLabels.DISCUSS.getEN());
        data.addAll(getProjectsIssuesByLabel(project.getGitlabProjectId(), GitLabProjectLabels.MODULE.getEN()));

        List issues = data.stream().distinct().collect(Collectors.toList());

        GetProjectDefaultIssuesVO getProjectDefaultIssuesVO = new GetProjectDefaultIssuesVO();
        getProjectDefaultIssuesVO.setModules(new ArrayList<>());
        getProjectDefaultIssuesVO.setDiscussions(new ArrayList<>());

        for (Object obj : issues) {
            JSONObject issue = (JSONObject) obj;
            GetProjectIssueVO getProjectIssueVO = new GetProjectIssueVO();
            getProjectIssueVO.setProject(project.getName());
            getProjectIssueVO.setProjectId(project.getId());
            getProjectIssueVO.setState(issue.getString("state"));
            getProjectIssueVO.setTitle(issue.getString("title"));
            getProjectIssueVO.setWebUrl(issue.getString("web_url"));

            JSONArray labels = issue.getJSONArray("labels");
            for (Object o : labels) {
                String label = (String) o;
                if (GitLabProjectLabels.MODULE.getEN().equals(label)) {
                    getProjectDefaultIssuesVO.getModules().add(getProjectIssueVO);
                } else if (GitLabProjectLabels.DISCUSS.getEN().equals(label)) {
                    getProjectDefaultIssuesVO.getDiscussions().add(getProjectIssueVO);
                }
            }
        }

        return getProjectDefaultIssuesVO;
    }

    private void deduplicatedCommitsByDate(List<GitLabCommitDTO> commits) {
        String lastDate = null;

        Iterator<GitLabCommitDTO> iterator = commits.iterator();

        while (iterator.hasNext()) {
            GitLabCommitDTO commit = iterator.next();
            Date committedDate = DateUtils.parseISO8601(commit.getCommittedDate());
            // 解析成yyyyMMdd
            String committedDateString = DateUtils.formatDate(committedDate, "yyyyMMdd");
            if (lastDate == null || !lastDate.equals(committedDateString)) lastDate = committedDateString;
            else {
                // 移除该项
                iterator.remove();
            }
        }
    }

    double addContributionScore(double score, double addVal) {
        if (score + addVal >= 10) {
            return 10.0;
        }
        return score + addVal;
    }

    @Override
    public void confirmDefence(Integer projectId) {
        ProjectWithMembersDetailsAndStatusPO project = projectPOMapper.selectFirstProjectByProjectId(projectId);
        // 检查状态
        checkStatus(project, ProjectStatus.DEVELOPING);
        // 检查权限
        masterAuth(project.getMembersPO());

        Date developmentStartAt = project.getStatusPO().getUpdateTime();
        Date developmentEndAt = new Date();
        // 获取commits数据
        String commitsDataString = gitlabHttpRequest.get(GitLabHttpRequestAuthMode.USER_AUTH,
                String.format("/projects/%d/repository/commits", project.getGitlabProjectId()));
        List<GitLabCommitDTO> commits = JSON.parseArray(commitsDataString, GitLabCommitDTO.class);
        double contributionScore = calculateContributionScore(10.0, project.getName(),
                developmentStartAt, developmentEndAt, commits, false);


        ProjectScorePO projectScorePO = new ProjectScorePO();

        projectScorePO.setProjectId(projectId);
        projectScorePO.setContribution(contributionScore);
        projectScorePOMapper.insertSelective(projectScorePO);

        // 更新状态
        ProjectStatusPO status = new ProjectStatusPO();
        status.setProjectId(projectId);
        status.setStatus(ProjectStatus.DEFENDING.getStatus());
        status.setUpdateTime(new Date());
        projectStatusPOMapper.insertSelective(status);

    }

    /**
     * 计算贡献得分
     *
     * @param totalScore      得分上限，如设置10.0分则累计加到10分后不在继续累加
     * @param projectName     项目名称
     * @param startAt         开始时间
     * @param endAt           结束时间
     * @param commits         从GitLab中获取的commits历史数据
     * @param isAllowNoCommit 在项目没有commit数据时是否抛出异常
     * @return 贡献得分
     */
    @SuppressWarnings("SameParameterValue")
    private Double calculateContributionScore(double totalScore,
                                              String projectName,
                                              Date startAt,
                                              Date endAt, List<GitLabCommitDTO> commits,
                                              boolean isAllowNoCommit) {
        long developingDays = DateUtils.getDifferenceDays(startAt, endAt);
        double averageScore = totalScore / (double) developingDays;

        double contributionScore = 0.0;

        /*
         * 如果开发时长大于5天则按照前20%天得分权值为2，后20%天得分权值为0.5评分
         * 否则按照全部算加权为2
         */
        int partialDays;
        if (developingDays > 5) {
            partialDays = new BigDecimal(0.2 * (double) developingDays).setScale(0, RoundingMode.UP).intValue();
        } else partialDays = (int) developingDays;

        Date topFifthOfDay = DateUtils.addDay(startAt, partialDays);
        Date posteriorFifthOfDay = DateUtils.addDay(endAt, -partialDays);

        if (!isAllowNoCommit && commits.size() == 0) throw new ForbiddenException("未进行过代码提交");

        // 根据时间去重
        deduplicatedCommitsByDate(commits);

        log.debug("项目: {}，开发过程持续了{}天，平均每天得{}分", projectName, developingDays, averageScore);

        // 计算总得分
        for (GitLabCommitDTO commit : commits) {
            Date committedDate = DateUtils.parseISO8601(commit.getCommittedDate());
            assert committedDate != null;
            if (DateUtils.isBetween(startAt, topFifthOfDay, committedDate)) {
                log.debug("commit: {}, 在前20%天中，权值为2", commit.getId());
                contributionScore = addContributionScore(contributionScore, averageScore * 2);
            } else if (!DateUtils.isBetween(posteriorFifthOfDay, endAt, committedDate)) {
                log.debug("commit: {}, 在中间权值为1", commit.getId());
                contributionScore = addContributionScore(contributionScore, averageScore);
            } else {
                log.debug("commit: {}, 在后20%天中，权值为0.5", commit.getId());
                contributionScore = addContributionScore(contributionScore, averageScore * 0.5);
            }
        }

        log.debug("最终得分为: {}", contributionScore);

        return contributionScore;

    }

    private String getProjectTotalScore(Double contribution, Double codeQ, Double defence) {
        if (contribution == null || codeQ == null || defence == null) return "0";
        return String.format("%.2f", contribution + 0.3 * codeQ + 0.6 * defence);
    }

    @Override
    public List<GetProjectScoreVO> getProjectScore(Integer projectId) {
        ProjectWithMembersDetailsAndStatusPO project = projectPOMapper.selectFirstProjectByProjectId(projectId);
        // 检查状态
//        checkStatus(project.getStatusPO(), ProjectStatus.DEFENDING);

        // 权限检测
        memberAuth(project.getMembersPO());

        ProjectScorePO projectScorePO = projectScorePOMapper.selectByPrimaryKey(projectId);

        GetProjectScoreVO contribution = new GetProjectScoreVO();
        contribution.setItem("日均贡献度");
        contribution.setStandard("根据成员每日代码贡献度打分，项目时间分配越平均分数越高");
        contribution.setScore(String.format("%.2f", projectScorePO.getContribution()));

        String commitsDataString = gitlabHttpRequest.get(GitLabHttpRequestAuthMode.USER_AUTH,
                String.format("/projects/%d/repository/commits", project.getGitlabProjectId()));
        List<GitLabCommitDTO> commits = JSON.parseArray(commitsDataString, GitLabCommitDTO.class);
        if (commits.size() == 0) throw new ForbiddenException("未进行过代码提交");
        GitLabCommitDTO latestCommit = commits.get(0);
        String sonarReportUrl = SONAR_REPORT + latestCommit.getId();

        GetProjectScoreVO codeQuality = new GetProjectScoreVO();
        codeQuality.setItem("代码质量");
        codeQuality.setStandard("由自动化测评系统根据代码风格、单元测试覆盖度以及代码缺陷进行评估，测评报告: " + sonarReportUrl);
        codeQuality.setScore(projectScorePO.getCodeQuality() == null
                ? "未打分" : String.format("%.2f", projectScorePO.getCodeQuality()));

        GetProjectScoreVO defence = new GetProjectScoreVO();
        defence.setItem("答辩");
        defence.setStandard("由导师打分根据现场演示及答辩打分");
        defence.setScore(projectScorePO.getDefence() == null
                ? "未打分" : String.format("%.2f", projectScorePO.getDefence()));

        GetProjectScoreVO total = null;

        if (!"未打分".equals(codeQuality.getScore()) && !"未打分".equals(defence.getScore())) {
            total = new GetProjectScoreVO();
            total.setItem("总分");
            total.setStandard("日均贡献度占比10%, 代码质量占比30%，答辩占比60%");
            total.setScore(getProjectTotalScore(projectScorePO.getContribution(),
                    projectScorePO.getCodeQuality(), projectScorePO.getDefence()));
        }

        GetProjectScoreVO finalTotal = total;
        return new ArrayList<GetProjectScoreVO>() {
            private static final long serialVersionUID = 1008421696565537636L;

            {
                add(contribution);
                add(codeQuality);
                add(defence);
                if (finalTotal != null) add(finalTotal);
            }
        };
    }

    @Override
    public void markDefence(Integer projectId, MarkProjectDTO markProjectDTO) {
        ProjectWithMembersDetailsAndStatusPO project = projectPOMapper.selectFirstProjectByProjectId(projectId);
        // 检测状态
        checkStatus(project, ProjectStatus.DEFENDING);
        // 检测权限
        tutorAuth(project.getMembersPO());

        ProjectScorePO projectScore = projectScorePOMapper.selectByPrimaryKey(projectId);
        if (projectScore == null) {
            log.error("project_score表异常，ID={}项目数据不存在", projectId);
            throw new InternalErrorException("服务器异常");
        }

        projectScore.setCodeQuality(markProjectDTO.getCodeQualityScore());
        projectScore.setDefence(markProjectDTO.getDefenceScore());
        projectScore.setSubmitDate(new Date());
        projectScorePOMapper.updateByPrimaryKey(projectScore);

        // 更新状态
        ProjectStatusPO status = new ProjectStatusPO();
        status.setProjectId(projectId);
        status.setStatus(ProjectStatus.CLOSED.getStatus());
        status.setUpdateTime(new Date());
        projectStatusPOMapper.insertSelective(status);
    }

}
