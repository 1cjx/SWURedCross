package com.jx.service;

import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.WxUserDto;
import com.jx.domain.dto.WxUserRegisterDto;


public interface WeiXinLoginService {
    ResponseResult login(WxUserDto wxUserDto);

    ResponseResult logout();

    ResponseResult register(WxUserRegisterDto wxUserRegisterDto);
}
