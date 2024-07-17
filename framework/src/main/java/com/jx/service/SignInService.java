package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.AddSignInDto;
import com.jx.domain.entity.SignIn;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * (SignIn)表服务接口
 *
 * @author makejava
 * @since 2023-10-19 11:01:48
 */
public interface SignInService extends IService<SignIn> {

    ResponseResult getSignInList(Long pageNum,Long pageSize);

    ResponseResult addSignIn(AddSignInDto addSignInDto);

    ResponseResult QRCodeSignIn(Long signInId);

    ResponseResult getChildrenSign(Long activityAssignmentId);

    ResponseResult getQRCodeSignInList(Long signInId);

    void getQRCode(Long signInId, HttpServletResponse response) throws IOException;
}

