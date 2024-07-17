package com.jx.service;

import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.LoginUserDto;

public interface AdminLoginService {

    public ResponseResult login(LoginUserDto user);

    public ResponseResult logout();
}
