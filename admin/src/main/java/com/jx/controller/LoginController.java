package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.domain.dto.LoginUserDto;
import com.jx.domain.entity.Department;
import com.jx.domain.entity.LoginUser;
import com.jx.domain.entity.Role;
import com.jx.domain.entity.User;
import com.jx.domain.vo.AdminUserInfoVo;
import com.jx.domain.vo.MenuVo;
import com.jx.domain.vo.RoutersVo;
import com.jx.domain.vo.UserInfoVo;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.service.AdminLoginService;
import com.jx.service.DepartmentService;
import com.jx.service.MenuService;
import com.jx.service.RoleService;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


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
    @ApiOperation("用户登录")
    @PostMapping("/admin/login")
    public ResponseResult login(@RequestBody LoginUserDto user){
        return loginService.login(user);
    }
    @ApiOperation("用户登出")
    @PostMapping("/admin/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
    @ApiOperation("登陆时查询用户个人信息")
    @GetMapping("/getInfo")
    public ResponseResult getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        User user = loginUser.getUser();
        //根据用户id查询角色信息
        Role role = roleService.getById(user.getRoleId());
        //获取用户信息
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        userInfoVo.setDepartment(departmentService.getById(user.getDepartmentId()).getName());
        userInfoVo.setRole(role.getRoleName());
        //封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,role.getRoleName(),userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    @ApiOperation("登陆时查询用户路由")
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouter(){
        //查询menu结果是tree的形式
        Long userId = SecurityUtils.getUserId();
        List<MenuVo>menus = menuService.selectRouterMenuTreeByUserID(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

}
