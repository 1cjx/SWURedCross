package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jx.constants.SystemConstants;
import com.jx.domain.entity.LoginUser;
import com.jx.domain.entity.Menu;
import com.jx.domain.entity.User;
import com.jx.mapper.MenuMapper;
import com.jx.mapper.UserMapper;
import com.jx.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,userId);
        User user = userMapper.selectOne(queryWrapper);
        //判断是否查到用户  如果没查到抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //查询用户权限
        //只有后台才会进入这里，判断是否允许进入后台
        //是就查询用户权限
        if(user.getType().equals(SystemConstants.PERMISSION_TO_ADMIN)){
            List<String> list = new ArrayList<>();
            //如果用户的id是设计者id，就直接返回所有权限
                list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,list);
        }
        //否则就拦截
        else{
            throw new RuntimeException("当前账号无权进入后台管理系统");
        }
    }
}