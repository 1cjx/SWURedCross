package com.jx.service;

import com.jx.domain.ResponseResult;
import com.jx.domain.dto.WxUserDto;
import com.jx.domain.dto.WxUserRegisterDto;
import com.jx.domain.entity.User;


public interface WeiXinLoginService {
    ResponseResult login(WxUserDto wxUserDto);

    ResponseResult logout();

    ResponseResult register(WxUserRegisterDto wxUserRegisterDto);
}
