package com.niuxuewei.lucius.service;

import com.niuxuewei.lucius.entity.dto.*;
import com.niuxuewei.lucius.entity.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface IProjectService {

    ApplicationUploadVO applicationUpload(MultipartFile file);

    CreateProjectVO createProject(CreateProjectDTO createProjectDTO);

    void deleteProject(Integer projectId);

    List<? extends GetProjectsVO> getProjects(String type);

    GetProjectDetailsVO getProjectDetails(Integer id);

    void rejectProjectApplication(Integer id, RejectProjectApplicationDTO rejectProjectApplicationDTO);

    void reapplyProject(ReapplyProjectDTO reapplyProjectDTO);

    void acceptProjectApplication(Integer id);

    List<GetProjectIssueVO> getProjectDiscuss();

    GetProjectDefaultIssuesVO getProjectDefaultIssues(Integer id);

    void confirmDefence(Integer projectId);

    List<GetProjectScoreVO> getProjectScore(Integer projectId);

    void markDefence(Integer projectId, MarkProjectDTO markProjectDTO);

    Double calculateContributionScore(double totalScore,
                                      String projectName,
                                      Date startAt,
                                      Date endAt, List<GitLabCommitDTO> commits,
                                      boolean isAllowNoCommit);

}
