package com.jx.service;

import com.jx.domain.ResponseResult;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {
    public ResponseResult createToken();
    public ResponseResult checkToken(HttpServletRequest request);
}
