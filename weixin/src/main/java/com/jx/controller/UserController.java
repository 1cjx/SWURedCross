package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddUserDto;
import com.jx.domain.vo.UserInfoVo;
import com.jx.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "用户相关接口")
@RequestMapping("/weixin/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 获取用户信息
     * @return
     */
    @SystemLog(businessName = "查询用户个人信息",type="1")
    @GetMapping("/getUserInfo")
    public ResponseResult getUserInfo(){
        return userService.getUserInfo();
    }

    /**
     * 获取用户参与的活动数目与总志愿时长
     * @return
     */
    @SystemLog(businessName = "查询用户参与的活动数目与总志愿时长",type="1")
    @GetMapping("/getUserActivityNumsAndVolunteerTimes")
    public ResponseResult getUserActivityNumsAndVolunteerTimes(){
        return userService.getUserActivityNumsAndVolunteerTimes();
    }

    /**
     * 修改个人信息
     * @param addUserDto
     * @return
     */
    @SystemLog(businessName = "修改个人信息",type="1")
    @PutMapping("/updateUserInfo")
    public ResponseResult updateUserInfo(@RequestBody AddUserDto addUserDto){
        return userService.updateUser(addUserDto);
    }
}
