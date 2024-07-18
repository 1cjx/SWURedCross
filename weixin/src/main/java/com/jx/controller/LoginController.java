package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.WxUserDto;
import com.jx.domain.dto.WxUserRegisterDto;
import com.jx.service.WeiXinLoginService;
import io.swagger.annotations.Api;
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
    @SystemLog(businessName = "用户登录",type="1")
    @PostMapping("/login")
    public ResponseResult login(@RequestBody WxUserDto wxUserDto){
        return weiXinLoginService.login(wxUserDto);
    }
    @SystemLog(businessName = "用户登出",type="1")
    @PostMapping("/logout")
    public ResponseResult logout(){
        return weiXinLoginService.logout();
    }
    @SystemLog(businessName = "用户注册",type="1")
    @PostMapping("/register")
    public ResponseResult register(@RequestBody WxUserRegisterDto wxUserRegisterDto){
        return weiXinLoginService.register(wxUserRegisterDto);
    }
}