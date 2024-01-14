package com.jx.service;

import com.jx.domain.ResponseResult;
import com.jx.domain.dto.LoginUserDto;
import com.jx.domain.entity.User;

public interface AdminLoginService {

    public ResponseResult login(LoginUserDto user);

    public ResponseResult logout();
}
