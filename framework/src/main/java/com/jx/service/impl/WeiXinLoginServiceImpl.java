package com.jx.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jx.constants.SystemConstants;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.WxUserDto;
import com.jx.domain.dto.WxUserRegisterDto;
import com.jx.domain.entity.User;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.UserMapper;
import com.jx.properties.WeixinProperties;
import com.jx.service.UserService;
import com.jx.service.WeiXinLoginService;
import com.jx.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

@Service("WeiXinLoginService")
public class WeiXinLoginServiceImpl implements WeiXinLoginService {
    @Autowired
    WeixinProperties weixinProperties;
    @Autowired
    WeiXinLoginService weiXinLoginService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    HttpClientUtil httpClientUtil;
    @Autowired
    RedisCache redisCache;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult login(WxUserDto wxUserDto) {
        String openid = getOpenId(wxUserDto.getCode());
        //插入用户到数据库 假如是 用户不存在 我们插入用户 如果用户存在 我们更新用户
        User resultWxuserInfo = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid,openid));
        //如果resultWxuserInfo不为空 说明该微信绑定了账户
        if(resultWxuserInfo!=null){
            //更新最后登录时间
            resultWxuserInfo.setLastlogindate(new Date());
            //利用jwt生成token返回到前端
            String token = JwtUtil.createJWT(resultWxuserInfo.getId().toString(),24*60 * 60 *7*1000L);//七天之内不失效
            redisCache.setCacheObject("weixinLogin:"+resultWxuserInfo.getId(),resultWxuserInfo);
            userService.updateById(resultWxuserInfo);
            return ResponseResult.okResult(token);
        }
        //如果为空 说明该微信没有绑定账户
        //此时根据传入的id、姓名查询用户
        else if(wxUserDto.getId()!=null&&wxUserDto.getName()!=null){
            User user = userMapper.getUserByInfo(wxUserDto.getId(),wxUserDto.getName());
            if(Objects.isNull(user)){
                throw new SystemException(AppHttpCodeEnum.USER_NOT_EXIT);
            }
            if(StringUtils.hasText(user.getOpenid())){
                throw new SystemException(AppHttpCodeEnum.USER_WX_IS_BIND);
            }
            user.setOpenid(openid);
            user.setIsBind(SystemConstants.USER_IS_BINDING);
            user.setLastlogindate(new Date());
            user.setRegisterdate(new Date());
            userService.updateById(user);
            //利用jwt生成token返回到前端
            String token = JwtUtil.createJWT(user.getId().toString());
            //把登录信息存入redis中
            redisCache.setCacheObject("weixinLogin:"+user.getId(),user);
            return ResponseResult.okResult(token);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.USER_IS_NOT_BIND);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("weixinLogin:"+userId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(WxUserRegisterDto wxUserRegisterDto) {
        //TODO validation参数校验
        if(Objects.isNull(wxUserRegisterDto.getId())){
            throw new SystemException(AppHttpCodeEnum.ID_NOT_NULL);
        }
        if(!StringUtils.hasText(wxUserRegisterDto.getName())){
            throw new SystemException(AppHttpCodeEnum.NAME_NOT_NULL);
        }
        if(!StringUtils.hasText(wxUserRegisterDto.getSex())){
            throw new SystemException(AppHttpCodeEnum.SEX_NOT_NULL);
        }
        if(!StringUtils.hasText(wxUserRegisterDto.getPhoneNumber())){
            throw new SystemException(AppHttpCodeEnum.PHONE_NUMBER_NOT_NULL);
        }
        if(!StringUtils.hasText(wxUserRegisterDto.getQq())){
            throw new SystemException(AppHttpCodeEnum.QQ_NOT_NULL);
        }
        if(!StringUtils.hasText(wxUserRegisterDto.getCode())){
            throw new SystemException(AppHttpCodeEnum.CODE_NOT_NULL);
        }
        //对数据进行是否存在的判断
        String openid = getOpenId(wxUserRegisterDto.getCode());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getOpenid,openid);
        int cnt = userService.count(lambdaQueryWrapper);
        if(cnt>0){
            throw new SystemException(AppHttpCodeEnum.WX_IS_BIND);
        }
        User newUser = BeanCopyUtils.copyBean(wxUserRegisterDto, User.class);
        String encodePassword = passwordEncoder.encode(SystemConstants.DEFAULT_PASSWD);
        newUser.setPassword(encodePassword);
        newUser.setStatus(SystemConstants.STATUS_NORMAL);
        newUser.setOpenid(openid);
        newUser.setIsBind("1");
        newUser.setCollegeId(Long.valueOf(String.valueOf(newUser.getId()).substring(6,9)));
        newUser.setDepartmentId(SystemConstants.OUT_DEPARTMENT);
        newUser.setType(SystemConstants.NOT_ADMIN);
        newUser.setTitleId(SystemConstants.ROLE_IS_VOLUNTEER);
        newUser.setRegisterdate(new Date());
        userService.save(newUser);
        return ResponseResult.okResult();
    }

    public String getOpenId(String code){
            String jscode2sessionUrl=weixinProperties.getJscode2sessionUrl()+
                    "?appid="+weixinProperties.getAppid()+
                    "&secret="+weixinProperties.getSecret()+
                    "&js_code="+code+"&grant_type=authorization_code";
            String result  = httpClientUtil.sendHttpGet(jscode2sessionUrl);
            JSONObject jsonObject = JSON.parseObject(result);
            String openid = jsonObject.get("openid").toString();
            return openid;
    }
}
