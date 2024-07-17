package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.bean.LoginUser;
import com.jx.domain.dto.LoginUserDto;
import com.jx.domain.entity.*;
import com.jx.domain.vo.AdminUserInfoVo;
import com.jx.domain.vo.MenuVo;
import com.jx.domain.vo.RoutersVo;
import com.jx.domain.vo.UserInfoVo;
import com.jx.service.*;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.SecurityUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api(tags = "用户登录/登出相关接口")
public class LoginController {
    @Autowired
    AdminLoginService loginService;
    @Autowired
    MenuService menuService;
    @Autowired
    RoleService roleService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    TitleService titleService;
    @SystemLog(businessName = "用户登录",type="2")
    @PostMapping("/admin/login")
    public ResponseResult login(@RequestBody LoginUserDto user){
        return loginService.login(user);
    }
    @SystemLog(businessName = "用户登出",type="2")
    @PostMapping("/admin/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
    @SystemLog(businessName = "登陆时查询用户个人信息",type="2")
    @GetMapping("/getInfo")
    public ResponseResult getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        User user = loginUser.getUser();
        //根据用户id查询角色信息
        Title title = titleService.getById(user.getTitleId());
        //获取用户信息
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        userInfoVo.setDepartment(departmentService.getById(user.getDepartmentId()).getName());
        userInfoVo.setTitle(title.getName());
        //封装数据返回
        //TODO 看一下返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,title.getName(),userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    @SystemLog(businessName = "登陆时查询用户路由",type="2")
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouter(){
        //查询menu结果是tree的形式
        Long userId = SecurityUtils.getUserId();
        List<MenuVo>menus = menuService.selectRouterMenuTreeByUserID(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

}
