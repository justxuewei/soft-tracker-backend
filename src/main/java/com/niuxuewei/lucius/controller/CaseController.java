package com.niuxuewei.lucius.controller;

import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.core.result.ResultBuilder;
import com.niuxuewei.lucius.entity.dto.CreateCaseDTO;
import com.niuxuewei.lucius.service.ICaseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

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

}
