package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.ListSignInDto;
import com.jx.domain.entity.SignInUser;

import javax.servlet.http.HttpServletResponse;


/**
 * (SigninUser)表服务接口
 *
 * @author makejava
 * @since 2023-10-19 11:01:39
 */
public interface SignInUserService extends IService<SignInUser> {

    ResponseResult listSignIns(Long pageNum, Long pageSize, ListSignInDto listSignInDto);

    void export(HttpServletResponse httpServletResponse, ListSignInDto listSignInDto);
}

