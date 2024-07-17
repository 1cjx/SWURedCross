package com.jx.controller;

import com.jx.domain.bean.ResponseResult;
import com.jx.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/token")
@Api(tags = "幂等性相关接口")
public class TokenController {
    @Autowired
    TokenService tokenService;
    @ApiOperation("创建请求token")
    @GetMapping("/create")
    public ResponseResult createToken(){
        return tokenService.createToken();
    }
    @ApiOperation("校验是否有请求token")
    @PostMapping("/check")
    public ResponseResult checkToken(HttpServletRequest request){
        return tokenService.checkToken(request);
    }
}
