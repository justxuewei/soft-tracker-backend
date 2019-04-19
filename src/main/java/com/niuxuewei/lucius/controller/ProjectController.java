package com.niuxuewei.lucius.controller;

import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.core.result.ResultBuilder;
import com.niuxuewei.lucius.entity.dto.CreateProjectDTO;
import com.niuxuewei.lucius.entity.dto.MarkProjectDTO;
import com.niuxuewei.lucius.entity.dto.ReapplyProjectDTO;
import com.niuxuewei.lucius.entity.dto.RejectProjectApplicationDTO;
import com.niuxuewei.lucius.service.IProjectService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
public class ProjectController {

    @Resource
    private IProjectService projectService;

    @PostMapping("/project/application_upload")
    public Result upload(@RequestParam("file")MultipartFile file) {
        return ResultBuilder.SuccessResult(projectService.applicationUpload(file));
    }

    @PostMapping("/project")
    public Result createProject(@Valid @RequestBody CreateProjectDTO createProjectDTO) {
        return ResultBuilder.SuccessResult(projectService.createProject(createProjectDTO));
    }

    @DeleteMapping("/project")
    public Result deleteProject(@RequestParam Integer id) {
        projectService.deleteProject(id);
        return ResultBuilder.SuccessResult();
    }

    @GetMapping("/projects/{type}")
    public Result getProjects(@PathVariable String type) {
        return ResultBuilder.SuccessResult(projectService.getProjects(type));
    }

    @GetMapping("/project/{id}")
    public Result getProjectDetails(@PathVariable Integer id) {
        return ResultBuilder.SuccessResult(projectService.getProjectDetails(id));
    }

    @PostMapping("/project/reject/{id}")
    public Result rejectProjectApplication(@PathVariable Integer id,
                                           @Valid @RequestBody RejectProjectApplicationDTO rejectProjectApplicationDTO) {
        projectService.rejectProjectApplication(id, rejectProjectApplicationDTO);
        return ResultBuilder.SuccessResult();
    }

    @PostMapping("/project/reapply")
    public Result reapplyProject(@Valid @RequestBody ReapplyProjectDTO reapplyProjectDTO) {
        projectService.reapplyProject(reapplyProjectDTO);
        return ResultBuilder.SuccessResult();
    }

    @PostMapping("/project/{id}/accept_application")
    public Result acceptProjectApplication(@PathVariable Integer id) {
        projectService.acceptProjectApplication(id);
        return ResultBuilder.SuccessResult();
    }

    @GetMapping("/project/discuss")
    public Result getProjectDiscuss() {
        return ResultBuilder.SuccessResult(projectService.getProjectDiscuss());
    }

    @GetMapping("/project/{id}/default_issues")
    public Result getProjectDefaultIssues(@PathVariable Integer id) {
        return ResultBuilder.SuccessResult(projectService.getProjectDefaultIssues(id));
    }

    @PostMapping("/project/{id}/confirm_defence")
    public Result confirmDefence(@PathVariable Integer id) {
        projectService.confirmDefence(id);
        return ResultBuilder.SuccessResult();
    }

    @GetMapping("/project/{id}/score")
    public Result getProjectScore(@PathVariable Integer id) {
        return ResultBuilder.SuccessResult(projectService.getProjectScore(id));
    }

    @PostMapping("/project/{id}/score")
    public Result markProject(@PathVariable Integer id, @Valid @RequestBody MarkProjectDTO markProjectDTO) {
        projectService.markDefence(id, markProjectDTO);
        return ResultBuilder.SuccessResult();
    }

}
