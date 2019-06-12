package com.niuxuewei.lucius.service;

import com.niuxuewei.lucius.entity.dto.CreateProjectDTO;
import com.niuxuewei.lucius.entity.dto.MarkProjectDTO;
import com.niuxuewei.lucius.entity.dto.ReapplyProjectDTO;
import com.niuxuewei.lucius.entity.dto.RejectProjectApplicationDTO;
import com.niuxuewei.lucius.entity.vo.*;
import org.springframework.web.multipart.MultipartFile;

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

}
