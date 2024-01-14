package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.domain.dto.WxUserDto;
import com.jx.domain.dto.WxUserRegisterDto;
import com.jx.domain.entity.User;
import com.jx.service.WeiXinLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weixin")
@Api(tags = "用户登录相关接口")
public class LoginController {
    @Autowired
    WeiXinLoginService weiXinLoginService;
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResponseResult login(@RequestBody WxUserDto wxUserDto){
        return weiXinLoginService.login(wxUserDto);
    }
    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public ResponseResult logout(){
        return weiXinLoginService.logout();
    }
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResponseResult register(@RequestBody WxUserRegisterDto wxUserRegisterDto){
        return weiXinLoginService.register(wxUserRegisterDto);
    }
}
