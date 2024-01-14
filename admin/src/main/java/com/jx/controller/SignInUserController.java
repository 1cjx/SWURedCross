package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.domain.dto.ListSignInDto;
import com.jx.domain.entity.SignInUser;
import com.jx.service.SignInService;
import com.jx.service.SignInUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/system/signInUser")
@Api(tags = "签到记录相关接口")
public class SignInUserController {
    @Autowired
    SignInUserService signInUserService;
    @ApiOperation("分页查询签到记录")
    @GetMapping("/list")
    public ResponseResult list(Long pageNum, Long pageSize, ListSignInDto listSignInDto){
        return signInUserService.listSignIns(pageNum,pageSize,listSignInDto);
    }
    @ApiOperation("导出当前查询的所有签到记录为excel")
    @GetMapping("/export")
    public void export(HttpServletResponse httpServletResponse, ListSignInDto listSignInDto){
        signInUserService.export(httpServletResponse,listSignInDto);
    }
}
