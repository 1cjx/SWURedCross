package com.jx.service;

import com.jx.domain.bean.ResponseResult;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {
    public ResponseResult createToken();
    public ResponseResult checkToken(HttpServletRequest request);
}
