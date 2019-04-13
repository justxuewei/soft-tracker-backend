package com.niuxuewei.lucius.service;

import com.niuxuewei.lucius.entity.dto.CreateProjectDTO;
import com.niuxuewei.lucius.entity.vo.ApplicationUploadVO;
import com.niuxuewei.lucius.entity.vo.CreateProjectVO;
import org.springframework.web.multipart.MultipartFile;

public interface IProjectService {

    ApplicationUploadVO applicationUpload(MultipartFile file);

    CreateProjectVO createProject(CreateProjectDTO createProjectDTO);

    void deleteProject(Integer projectId);

}
