package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddSignInDto;
import com.jx.service.SignInService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/signIn")
@Api(tags = "签到相关接口")
public class SignInController {
    @Autowired
    SignInService signInService;

    /**
     * 分页查询用户发起的签到
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("分页查询用户发起的签到")
    @GetMapping("/getSignInList")
    public ResponseResult getSignInList( @Param("pageNum")Long pageNum, @Param("pageSize")Long pageSize){
        return signInService.getSignInList(pageNum,pageSize);
    }

    /**
     * 根据签到类型、activityAssignmentId、签到开始时间、签到结束时间创建签到
     * @param addSignInDto
     * @return
     */
    @ApiOperation("创建签到")
    @PostMapping("/addSignIn")
    public ResponseResult addSignIn(@RequestBody AddSignInDto addSignInDto){
        return signInService.addSignIn(addSignInDto);
    }

    /**
     *  生成二维码
     * @param signInId 是签到id

     * */
    @GetMapping(value = "/getQRCode")
    @ApiOperation("生成活动详情二维码")
    public void getQRCode(Long signInId , HttpServletResponse response) throws IOException {
        signInService.getQRCode(signInId,response);
    }
    /**
     * 扫码签到
     * @param signInId
     * @return
     */
    @ApiOperation("扫码签到")
    @GetMapping("/QRCodeSignIn")
    public ResponseResult QRCodeSignIn(Long signInId){
        return signInService.QRCodeSignIn(signInId);
    }

    /**
     * 获取子签到
     * @param activityAssignmentId
     * @return
     */
    @ApiOperation("查询当前签到的子签到")
    @GetMapping("/getSignDetail")
    public ResponseResult getChildrenSign(Long activityAssignmentId){
        return signInService.getChildrenSign(activityAssignmentId);
    }
    @ApiOperation("查询最新扫码签到的十个用户")
    @GetMapping("/getQRCodeSignInList")
    public ResponseResult getQRCodeSignInList(Long signInId){
        return signInService.getQRCodeSignInList(signInId);
    }
}
