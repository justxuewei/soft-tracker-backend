package com.niuxuewei.lucius.controller;

import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.entity.dto.CreateCaseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/case")
public class CaseController {

    @PostMapping
    public Result createCase(@Valid @RequestBody CreateCaseDTO createCaseDTO) {
        // TODO: 检查身份
        return null;
    }

}
