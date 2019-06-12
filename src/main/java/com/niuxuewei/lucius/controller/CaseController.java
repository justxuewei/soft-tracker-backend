package com.niuxuewei.lucius.controller;

import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.core.result.ResultBuilder;
import com.niuxuewei.lucius.entity.dto.CreateCaseDTO;
import com.niuxuewei.lucius.entity.dto.EditCaseDTO;
import com.niuxuewei.lucius.service.ICaseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/case")
public class CaseController {

    @Resource
    private ICaseService caseService;

    @PostMapping
    public Result createCase(@Valid @RequestBody CreateCaseDTO createCaseDTO) {
        caseService.createCase(createCaseDTO);
        return ResultBuilder.SuccessResult();
    }

    /**
     * TODO: 添加分页功能
     */
    @GetMapping
    public Result getCases(@RequestParam(required = false) Integer tutorId) throws NoSuchAlgorithmException {
        return ResultBuilder.SuccessResult(caseService.getCases(tutorId));
    }

    @GetMapping("/details")
    public Result getCaseDetails(@RequestParam Integer caseId) throws NoSuchAlgorithmException {
        return ResultBuilder.SuccessResult(caseService.getCaseDetails(caseId));
    }

    @PostMapping("/edit")
    public Result editCase(@Valid @RequestBody EditCaseDTO editCaseDTO) {
        caseService.editCase(editCaseDTO);
        return ResultBuilder.SuccessResult();
    }

}
