package com.jx.aspect;

import com.alibaba.fastjson.JSON;
import com.jx.anatation.SystemLog;
import com.jx.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Aspect
@Slf4j
public class LogAspect {
    private static final ConcurrentHashMap<Long, StringBuilder> threadLoggers = new ConcurrentHashMap<>();
    //确定切点
    @Pointcut("@annotation(com.jx.anatation.SystemLog)")
    public void pt(){

    }
    //定义通知方法
    //环绕通知
    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret;
        StringBuilder logger = threadLoggers.computeIfAbsent(Thread.currentThread().getId(), k -> new StringBuilder());
        try {
            handlerBefore(joinPoint,logger);
            ret = joinPoint.proceed();
            handelerAfter(joinPoint,ret,logger);
        }
        catch(Throwable t){
            //打印异常信息
            logger.append("Error Reason   :"+t).append("\n");
            logger.append("==================================End=================================="+System.lineSeparator());
            log.error(String.valueOf(logger));
            logger = null;
            threadLoggers.remove(Thread.currentThread().getId());
            //抛出异常 使得全局异常拦截器可以返回给前端json格式
            throw t;
        }
        finally {
            if(!Objects.isNull(logger)){
                logger.append("==================================End=================================="+System.lineSeparator());
                log.info(String.valueOf(logger));
                threadLoggers.remove(Thread.currentThread().getId());
            }
        }
        return ret;
    }

    private void handelerAfter(ProceedingJoinPoint joinPoint,Object ret,StringBuilder logger) {
        SystemLog SystemLog = getSystemLog(joinPoint);
        if(!SystemLog.businessName().contains("导出")) {
            // 打印出参
            logger.append("Response       :"+JSON.toJSONString(ret)).append("\n");
//            log.info("Response       : {}",JSON.toJSONString(ret));
        }
    }

    private void handlerBefore(ProceedingJoinPoint joinPoint,StringBuilder logger) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //获取被增强方法上的注解对象
        SystemLog SystemLog = getSystemLog(joinPoint);
            logger.append("\n").append("==================================Start==================================").append("\n");
            // 打印请求 URL
            logger.append("URL            :"+request.getRequestURL()).append("\n");
            // 打印描述信息
            logger.append("BusinessName   :"+SystemLog.businessName()).append("\n");
            // 打印执行用户
            if(!SystemLog.businessName().equals("用户登录")){
                logger.append("ExecuteUser    :"+SecurityUtils.getLoginUser().getUser().getName()).append("\n");
            }
            // 打印 Http method
            logger.append("HTTP Method    :"+request.getMethod()).append("\n");
            // 打印调用 controller 的全路径以及执行方法
            logger.append("Class Method   :"+joinPoint.getSignature().getDeclaringTypeName()+"."+((MethodSignature) joinPoint.getSignature()).getName()).append("\n");
            // 打印请求的 IP
            logger.append("IP             :"+request.getRemoteHost()).append("\n");
        if(!SystemLog.businessName().equals("上传头像")&&
                !SystemLog.businessName().equals("通过模板excel数据批量新增用户")&&
                !SystemLog.businessName().contains("导出")&&
                !SystemLog.businessName().equals("生成活动详情二维码")) {
            // 打印请求入参
            logger.append("Request Args   :"+JSON.toJSONString(joinPoint.getArgs())).append("\n");
//            log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
        }
    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        SystemLog SystemLog = methodSignature.getMethod().getAnnotation(SystemLog.class);
        return SystemLog;
    }
}
