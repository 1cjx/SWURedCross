package com.jx.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.aliyun.oss.ServiceException;
import com.jx.domain.ResponseResult;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.service.TokenService;
import com.jx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.UUID;

@Service("TokenService")
public class TokenServiceImpl implements TokenService {
    @Autowired
    RedisCache redisCache;
    @Override
    public ResponseResult createToken() {
        //生成uuid当作token
        String token = UUID.randomUUID().toString().replaceAll("-","");
        redisCache.setCacheObject("checkToken",token);
        return ResponseResult.okResult(token);
    }

    @Override
    public ResponseResult checkToken(HttpServletRequest request) {
        //从请求头中获取token
        String token=request.getHeader("checkToken");
        if (StringUtils.isBlank(token)){
            //如果请求头token为空就从参数中获取
            token=request.getParameter("checkToken");
            //如果都为空抛出参数异常的错误
            if (StringUtils.isBlank(token)){
                throw new SystemException(AppHttpCodeEnum.NEED_TOKEN);
            }
        }
        //如果redis中不包含该token，说明token已经被删除了，抛出请求重复异常
        if (Objects.isNull(redisCache.getCacheObject("checkToken"))){
            throw new SystemException(AppHttpCodeEnum.REQUEST_AGAIN);
        }
        //删除token
        Boolean del=redisCache.deleteObject("checkToken");
        //如果删除不成功（已经被其他请求删除），抛出请求重复异常
        if (!del){
            throw new SystemException(AppHttpCodeEnum.REQUEST_AGAIN);
        }
        return ResponseResult.okResult();
    }
}
