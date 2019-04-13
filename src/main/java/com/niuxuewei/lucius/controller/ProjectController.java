package com.niuxuewei.lucius.controller;

import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.core.result.ResultBuilder;
import com.niuxuewei.lucius.entity.dto.CreateProjectDTO;
import com.niuxuewei.lucius.service.IProjectService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Resource
    private IProjectService projectService;

    @PostMapping("/application_upload")
    public Result upload(@RequestParam("file")MultipartFile file) {
        return ResultBuilder.SuccessResult(projectService.applicationUpload(file));
    }

    @PostMapping
    public Result createProject(@Valid @RequestBody CreateProjectDTO createProjectDTO) {
        return ResultBuilder.SuccessResult(projectService.createProject(createProjectDTO));
    }

    @DeleteMapping
    public Result deleteProject(@RequestParam Integer id) {
        projectService.deleteProject(id);
        return ResultBuilder.SuccessResult();
    }

}
