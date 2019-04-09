package com.niuxuewei.lucius.controller;

import com.niuxuewei.lucius.core.request.GitlabHttpRequest;
import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.core.result.ResultBuilder;
import com.niuxuewei.lucius.entity.dto.AddSSHKeyDTO;
import com.niuxuewei.lucius.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private GitlabHttpRequest gitlabHttpRequest;

    @GetMapping("/info")
    public Result getUserInfo() {
        return ResultBuilder.SuccessResult(userService.getUserInfo());
    }

    @GetMapping("/ssh")
    public Result getSSHKeys() {
        return ResultBuilder.SuccessResult(userService.getSSHKeys());
    }

    @PostMapping("/ssh")
    public Result addSSHKey(@Valid @RequestBody AddSSHKeyDTO addSSHKeyDTO) throws Exception {
        return ResultBuilder.SuccessResult(userService.addSSHKey(addSSHKeyDTO));
    }

    @DeleteMapping("/ssh")
    public Result deleteSSHKey(@RequestParam String keyId) {
        userService.deleteSSHKey(keyId);
        return ResultBuilder.SuccessResult();
    }

    @GetMapping("/access_key")
    public Result getAccessKey() {
        return ResultBuilder.SuccessResult(gitlabHttpRequest.getImpersonationToken());
    }

}
